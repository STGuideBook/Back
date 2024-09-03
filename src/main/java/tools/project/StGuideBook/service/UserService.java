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

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if(siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

}
