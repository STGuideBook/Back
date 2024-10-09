package tools.project.StGuideBook.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {

        boolean isAuthenticated = userService.authenticate(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        if(isAuthenticated) {
            request.getSession().setAttribute("username", loginRequestDTO.getUsername());
            return ResponseEntity.ok("로그인 성공.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 실패 : 사용자ID 혹은 비밀번호가 잘못되었습니다.");
        }
    }

    @PostMapping("/logout") // 로그아웃(세션 만료 방식)
    public ResponseEntity<String> logout(HttpServletRequest request) {

        request.getSession().invalidate();

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request) {
        userService.changePassword(request);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @GetMapping("/user/status") // 로그인 상태 확인
    public ResponseEntity<String> checkLoginStatus(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");

        if (username != null) {
            return ResponseEntity.ok("로그인 상태: " + username);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
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
