package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.SiteUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);
}
