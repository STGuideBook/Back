package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        UserRole userRole = UserRole.USER;
        SiteUser user = new SiteUser(username, email, passwordEncoder.encode(password), userRole);
        this.userRepository.save(user);
        return user;
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

}
