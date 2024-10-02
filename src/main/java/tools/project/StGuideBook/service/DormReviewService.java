package tools.project.StGuideBook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.Dorm;
import tools.project.StGuideBook.domain.DormReview;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.exception.UnauthorizedException;
import tools.project.StGuideBook.repository.DormRepository;
import tools.project.StGuideBook.repository.DormReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DormReviewService {

    private final DormReviewRepository dormReviewRepository;
    private final DormRepository dormRepository;

    @Autowired
    public DormReviewService(DormReviewRepository dormReviewRepository, DormRepository dormRepository) {
        this.dormReviewRepository = dormReviewRepository;
        this.dormRepository = dormRepository;
    }

    public DormReview addDormReview(Long dormId, String username, String comment) {
        Dorm dorm = dormRepository.findById(dormId).orElseThrow(() ->
                new IllegalArgumentException("기숙사를 찾을 수 없습니다"));

        DormReview dormReview = new DormReview(dorm, username, LocalDateTime.now(), comment);

        return dormReviewRepository.save(dormReview);
    }

    public List<DormReview> getReviewsByDorm(Long dormId) {
        return dormReviewRepository.findByDormDormId(dormId);
    }

    public void deleteDormReview(Long dormId, SiteUser user) {
        DormReview dormReview = dormReviewRepository.findById(dormId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾지 못했습니다."));

        if (!dormReview.getUsername().equals(user.getUsername()) && !UserRole.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        dormReviewRepository.delete(dormReview);
    }

}
