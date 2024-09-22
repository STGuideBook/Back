package tools.project.StGuideBook.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.dto.DeleteUserDTO;
import tools.project.StGuideBook.dto.LoginRequestDTO;
import tools.project.StGuideBook.dto.UserCreateDTO;
import tools.project.StGuideBook.service.UserService;

@RestController
public class AuthController { // 회원가입 및 로그인/아웃 기능에 대한 컨트롤러

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
}

    @PostMapping("/user/signup") // 회원가입
    public ResponseEntity<?> signup(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        if(!userCreateDTO.getPassword1().equals(userCreateDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            userService.create(userCreateDTO.getUsername(), userCreateDTO.getEmail(),
                    userCreateDTO.getPassword1());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(bindingResult.getAllErrors());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bindingResult.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login") // 로그인
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        boolean isAuthenticated = userService.authenticate(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        if(isAuthenticated) {
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
