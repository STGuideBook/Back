package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.ChangePasswordDTO;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    public void create(String username, String password, Integer student_Id) {
        UserRole userRole = UserRole.USER;
        SiteUser user = new SiteUser(username, passwordEncoder.encode(password), student_Id, userRole);
        this.userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        return siteUser.orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public boolean authenticate(String username, String password) {
        SiteUser user = this.getUser(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void grantAdminRole(String username) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("유저를 찾지 못했습니다"));

        user.changeRoleToAdmin();
        userRepository.save(user);
    }

    public void grantUserRole(String username) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("유저를 찾지 못했습니다"));

        user.changeRoleToUser();
        userRepository.save(user);
    }

    public List<SiteUser> getAllUser() {
        return userRepository.findAll();
    }

    public boolean deleteUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if(siteUser.isPresent()) {
            userRepository.delete(siteUser.get());
            return true;
        }
        return false;
    }

    public int getTotalUsers() {
        return (int) userRepository.count();
    }

    public int getTotalAdmins() {
        return userRepository.countByRole(UserRole.valueOf("ADMIN"));
    }

    public boolean verifyPassword(String username, String password) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void changePassword(ChangePasswordDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 잘못됐습니다.");
        }

        passwordValidator.validate(request.getNewPassword());

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("새로운 비밀번호가 기존 비밀번호와 같을 수 없습니다.");
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username); // 물리적 삭제이므로 북구 불가능.
    }
}
