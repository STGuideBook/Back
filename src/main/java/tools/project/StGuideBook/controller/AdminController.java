package tools.project.StGuideBook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/grant_admin")
    public ResponseEntity<String> grantAdmin(@RequestBody Map<String, String> requestBody) {

        String username = requestBody.get("username");

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다");
        }

        userService.grantAdminRole(username);

        return ResponseEntity.ok(username + "님에게 관리자 권한이 부여되었습니다.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/grant_user")
    public ResponseEntity<String> grantUser(@RequestBody Map<String, String> requestBody) {

        String username = requestBody.get("username");

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다");
        }

        userService.grantUserRole(username);

        return ResponseEntity.ok(username + "님이 일반 유저로 권한이 변경되었습니다.");
    }

    // 관리자 세부 기능 아직 개발 중
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("관리자 전용 대쉬보드. 전체 유저 관리 기능 넣을 예정");
    }

}
