package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.Dorm;

public interface DormRepository extends JpaRepository<Dorm, Long> {
}
