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
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController { // 모든 어드민 기능에 대한 컨트롤러

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/grant_admin") // 기존 유저에게 어드민 권한 부여(파라미터 전송)
    public ResponseEntity<String> grantAdmin(@RequestParam(name = "username") String username) {

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다");
        }

        userService.grantAdminRole(username);

        return ResponseEntity.ok(username + "님에게 관리자 권한이 부여되었습니다.");
    }

    @PostMapping("/grant_user") // 기존 어드민 유저로 변경(파라미터 전송)
    public ResponseEntity<String> grantUser(@RequestParam(name = "username") String username) {

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 공백일 수 없습니다");
        }

        userService.grantUserRole(username);

        return ResponseEntity.ok(username + "님이 일반 유저로 권한이 변경되었습니다.");
    }

    @GetMapping("/dashboard") // 어드민 대시보드
    public ResponseEntity<Map<String, Object>> dashboard() {
        // 대시보드에 전체 유저를 조회 가능한 페이지로 이동 가능해야함
        Map<String, Object> dashboardData = new HashMap<>();

        int totalUsers = userService.getTotalUsers();
        int totalAdmins = userService.getTotalAdmins();

        dashboardData.put("message", "관리자 전용 대시보드. 유저 및 관리자 수 조회 가능");
        dashboardData.put("totalUsers", totalUsers);
        dashboardData.put("totalAdmins", totalAdmins);

        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/user_list") // 유저 전체 조회
    public ResponseEntity<List<SiteUser>> getAllUsers() {
        // 유저 전체 조회 기능에서 해당 유저를 클릭하면 유저에 대한 정보를 변경하는 페이지로 이동 가능해야함
        List<SiteUser> user = userService.getAllUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user_detail") // 해당 유저만 조회
    public ResponseEntity<Optional<SiteUser>> getUsersByUsername(@RequestParam(name = "username") String username) {
        Optional<SiteUser> user = Optional.ofNullable(userService.getUser(username));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user_delete") // 유저 삭제 기능
    public ResponseEntity<String> deleteUser(@RequestParam(name = "username") String username) {
        boolean isDeleted = userService.deleteUser(username);

        if(isDeleted) {
            return ResponseEntity.ok(username + " 사용자가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자명의 사용자를 찾을 수 없습니다.");
        }
    }

}
