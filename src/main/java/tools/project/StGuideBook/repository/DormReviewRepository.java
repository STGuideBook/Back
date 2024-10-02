package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.DormReview;

import java.util.List;

public interface DormReviewRepository extends JpaRepository<DormReview, Long> {
    List<DormReview> findByDormDormId(Long dormId);
}

