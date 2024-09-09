package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.domain.TipPost;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.CommentDTO;
import tools.project.StGuideBook.dto.TipPostDTO;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.repository.TipPostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipPostService {

    private final TipPostRepository tipPostRepository;

    public List<TipPost> getList() {
        return this.tipPostRepository.findAll();
    }

    public TipPost getQuestion(Integer id) {
        Optional<TipPost> question = this.tipPostRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        TipPost tipPost = new TipPost(subject, content, LocalDateTime.now(), user);
        this.tipPostRepository.save(tipPost);
    }

    public TipPostDTO convertToDto(TipPost tipPost) {
        List<CommentDTO> commentDTOList = tipPost.getCommentList().stream()
                .map(comment -> new CommentDTO(comment.getContent()))
                .collect(Collectors.toList());

        return new TipPostDTO(tipPost.getSubject(), tipPost.getContent(), tipPost.getCreateDate(), commentDTOList);
    }
}
