package tools.project.StGuideBook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.dto.ChangePasswordDTO;
import tools.project.StGuideBook.dto.DeleteUserDTO;
import tools.project.StGuideBook.dto.LoginRequestDTO;
import tools.project.StGuideBook.dto.UserCreateDTO;
import tools.project.StGuideBook.service.PasswordValidator;
import tools.project.StGuideBook.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController { // 회원가입 및 로그인/아웃 기능에 대한 컨트롤러

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final PasswordValidator passwordValidator;

    @Autowired
    public AuthController(UserService userService, PasswordValidator passwordValidator) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
    }

    @PostMapping("/user/signup") // 회원가입
    public ResponseEntity<?> signup(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try { // 비밀번호 작성 시 일정 규칙을 따르도록 설정
            passwordValidator.validate(userCreateDTO.getPassword1());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("password1", "passwordInvalid", e.getMessage());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        if(!userCreateDTO.getPassword1().equals(userCreateDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            userService.create(userCreateDTO.getUsername(),
                    userCreateDTO.getPassword1(), userCreateDTO.getStudent_Id());
        } catch (DataIntegrityViolationException e) {
            logger.error("signup failed: 이미 등록된 사용자 입니다.", e);
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(bindingResult.getAllErrors());
        } catch (Exception e) {
            logger.error("예기치 못한 오류로 인하여 가입에 실패하였습니다.", e);
            bindingResult.reject("signupFailed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bindingResult.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login") // 로그인
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {

        // 인증 로직
        boolean isAuthenticated = userService.authenticate(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        // JSON 응답 준비
        Map<String, String> response = new HashMap<>();

        if (isAuthenticated) {
            // 세션에 사용자 이름 저장
            request.getSession().setAttribute("username", loginRequestDTO.getUsername());

            response.put("status", "success");
            response.put("message", "로그인 성공.");
            response.put("username", loginRequestDTO.getUsername());

            return ResponseEntity.ok(response); // 200 OK
        } else {
            // 실패 응답
            response.put("status", "fail");
            response.put("message", "로그인 실패 : 사용자ID 또는 비밀번호가 잘못되었습니다.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 Unauthorized
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션이 없으면 null 반환
        if (session != null) {
            session.invalidate(); // 세션이 존재할 경우에만 무효화
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "로그아웃 되었습니다.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request) {
        userService.changePassword(request);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @GetMapping("/user/status") // 로그인 상태 확인
    public ResponseEntity<Map<String, String>> checkLoginStatus(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        Map<String, String> response = new HashMap<>();

        if (username != null) {
            response.put("status", "success");
            response.put("message", "로그인 상태입니다.");
            response.put("username", username);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @DeleteMapping("/unregister/{username}") // 회원탈퇴
    public ResponseEntity<String> deleteUser(@PathVariable(name = "username") String username,
                                             @RequestBody DeleteUserDTO deleteUserDTO) {
        boolean isPasswordValid = userService.verifyPassword(username, deleteUserDTO.getPassword());

        if (isPasswordValid) {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }
    }
}
