package tools.project.StGuideBook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.Dorm;
import tools.project.StGuideBook.domain.DormReview;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.DormReviewDTO;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.exception.UnauthorizedException;
import tools.project.StGuideBook.repository.DormRepository;
import tools.project.StGuideBook.repository.DormReviewRepository;
import tools.project.StGuideBook.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DormReviewService {

    private final DormReviewRepository dormReviewRepository;
    private final DormRepository dormRepository;
    private final UserRepository userRepository;


    @Autowired
    public DormReviewService(DormReviewRepository dormReviewRepository,
                             DormRepository dormRepository, UserRepository userRepository) {
        this.dormReviewRepository = dormReviewRepository;
        this.dormRepository = dormRepository;
        this.userRepository = userRepository;
    }

    public DormReview addDormReview(Long dormId, String username, String comment) {
        Dorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new IllegalArgumentException("기숙사를 찾을 수 없습니다"));

        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        DormReview dormReview = new DormReview(dorm, user, LocalDateTime.now(), comment);

        return dormReviewRepository.save(dormReview);
    }

    public DormReviewDTO convertToDTO(DormReview dormReview) {
        return DormReviewDTO.fromEntity(dormReview);
    }

    public List<DormReview> getReviewsByDorm(Long dormId) {
        return dormReviewRepository.findByDormDormId(dormId);
    }

    public void deleteDormReview(Long dormId, SiteUser user) {
        DormReview dormReview = dormReviewRepository.findById(dormId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾지 못했습니다."));

        if (dormReview.getSiteUser().getUsername() == null || user.getUsername() == null || user.getRole() == null) {
            throw new UnauthorizedException("유효하지 않은 사용자 정보입니다.");
        }

        if (!dormReview.getSiteUser().getUsername().equals(user.getUsername()) && user.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        dormReviewRepository.delete(dormReview);
    }

}
