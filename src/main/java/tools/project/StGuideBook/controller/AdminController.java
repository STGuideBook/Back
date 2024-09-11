package tools.project.StGuideBook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/grant_admin")
    public ResponseEntity<String> grantAdmin(@RequestBody Map<String, String> requestBody) {

        String username = requestBody.get("username");

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다");
        }

        userService.grantAdminRole(username);

        return ResponseEntity.ok(username + "님에게 관리자 권한이 부여되었습니다.");
    }

    @PostMapping("/grant_user")
    public ResponseEntity<String> grantUser(@RequestBody Map<String, String> requestBody) {

        String username = requestBody.get("username");

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다");
        }

        userService.grantUserRole(username);

        return ResponseEntity.ok(username + "님이 일반 유저로 권한이 변경되었습니다.");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> dashboardData = new HashMap<>();

        int totalUsers = userService.getTotalUsers();
        int totalAdmins = userService.getTotalAdmins();

        dashboardData.put("message", "관리자 전용 대쉬보드. 유저 및 관리자 수 조회 가능");
        dashboardData.put("totalUsers", totalUsers);
        dashboardData.put("totalAdmins", totalAdmins);

        return ResponseEntity.ok(dashboardData);
    }


    @GetMapping("/user_list")
    public ResponseEntity<List<SiteUser>> getAllUsers() {
        List<SiteUser> user = userService.getAllUser();
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user_delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "username") String username) {
        boolean isDeleted = userService.deleteUser(username);

        if(isDeleted) {
            return ResponseEntity.ok(username + " 사용자가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자명의 사용자를 찾을 수 없습니다.");
        }
    }

}
