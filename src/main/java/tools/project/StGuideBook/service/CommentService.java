package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.domain.Comment;
import tools.project.StGuideBook.domain.TipPost;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.repository.CommentRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(TipPost tipPost, String content, SiteUser author) {
        Comment comment = new Comment(content, LocalDateTime.now(), tipPost, author);
        this.commentRepository.save(comment);
    }
}
