package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.controller.AuthController;
import tools.project.StGuideBook.domain.TipPost;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.CommentDTO;
import tools.project.StGuideBook.dto.TipPostDTO;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.exception.UnauthorizedException;
import tools.project.StGuideBook.repository.TipPostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TipPostService {

    private final TipPostRepository tipPostRepository;
    private static final Logger logger = LoggerFactory.getLogger(TipPostService.class);

    public List<TipPost> getList() {
        return this.tipPostRepository.findAll();
    }

    public TipPost getQuestion(Integer id) {
        Optional<TipPost> question = this.tipPostRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("질문을 찾지 못했습니다.");
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

    public TipPost updatePost(Integer id, TipPostDTO tipPostDTO, SiteUser user, LocalDateTime updateDate) {
        TipPost tipPost = tipPostRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾지 못했습니다."));

        // 작성자 확인
        if (!tipPost.getAuthor().equals(user)) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        // 수정할 내용 업데이트
        tipPost.setSubject(tipPostDTO.getSubject());
        tipPost.setContent(tipPostDTO.getContent());
        tipPost.setCreateDate(updateDate);

        logger.info("Create Date from DTO: " + tipPostDTO.getCreateDate());

        return tipPostRepository.save(tipPost);
    }

    public void deletePost(Integer id, SiteUser user) {
        TipPost tipPost = tipPostRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾지 못했습니다."));

        // 작성자 확인
        if (!tipPost.getAuthor().equals(user)) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        tipPostRepository.delete(tipPost);
    }
}
