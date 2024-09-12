package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.SiteUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);
    int countByRole(UserRole role);

    @Modifying
    @Transactional
    void deleteByUsername(String username);
}
