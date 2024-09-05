package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        SiteUser user = new SiteUser(username, email, passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

//    public SiteUser getUser(String username) {
//        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
//        if(siteUser.isPresent()) {
//            return siteUser.get();
//        } else {
//            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
//        }
//    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        return siteUser.orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public boolean authenticate(String username, String password) {
        SiteUser user = this.getUser(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

}
