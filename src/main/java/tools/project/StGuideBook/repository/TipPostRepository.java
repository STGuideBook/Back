package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.TipPost;

import java.util.List;

public interface TipPostRepository extends JpaRepository<TipPost, Integer> {

    TipPost findBySubject(String subject);
    TipPost findBySubjectAndContent(String subject, String content);
    List<TipPost> findBySubjectLike(String subject);
}
