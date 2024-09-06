package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
