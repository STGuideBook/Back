package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.TipPost;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.TipPostDTO;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.exception.UnauthorizedException;
import tools.project.StGuideBook.repository.TipPostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            throw new DataNotFoundException("질문을 찾지 못했습니다.");
        }
    }

    public void create(String subject, String content, SiteUser user) {

        TipPost tipPost = new TipPost(subject, content, LocalDateTime.now(), user);
        this.tipPostRepository.save(tipPost);
    }

    public TipPostDTO convertToDto(TipPost tipPost) {
        return new TipPostDTO(tipPost.getSubject(), tipPost.getContent(), tipPost.getCreateDate(),
                tipPost.getLikeCount(), tipPost.getAuthor().getStudent_Id());
    }

    public TipPost updatePost(Integer id, TipPostDTO tipPostDTO, SiteUser user, LocalDateTime updateDate) {
        TipPost tipPost = tipPostRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾지 못했습니다."));

        // 작성자 확인
        if (!tipPost.getAuthor().equals(user) && !UserRole.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }

        // 수정할 내용 업데이트
        tipPost.setSubject(tipPostDTO.getSubject());
        tipPost.setContent(tipPostDTO.getContent());
        tipPost.setCreateDate(updateDate);

        return tipPostRepository.save(tipPost);
    }

    public void deletePost(Integer id, SiteUser user) {
        TipPost tipPost = tipPostRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾지 못했습니다."));

        // 작성자 확인
        if (!tipPost.getAuthor().equals(user) && !UserRole.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        tipPostRepository.delete(tipPost);
    }

    public void toggleLike(Integer postId, SiteUser user) {
        TipPost tipPost = tipPostRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾지 못했습니다."));
        tipPost.toggleLike(user); // 좋아요 추가 또는 취소
        tipPostRepository.save(tipPost);
    }
}