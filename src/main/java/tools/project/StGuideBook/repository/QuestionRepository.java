package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
}
