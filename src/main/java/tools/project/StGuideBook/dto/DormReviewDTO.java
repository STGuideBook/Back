package tools.project.StGuideBook.dto;

import lombok.Getter;
import tools.project.StGuideBook.domain.DormReview;

@Getter
public class DormReviewDTO {

    private final Long reviewId;          // 리뷰 ID
    private final DormDTO dormDTO;        // 기숙사 정보
    private final String username;        // 작성자 이름
    private final String comment;         // 리뷰 내용
    private final String createDate;      // 생성 날짜
    private final Integer studentId;      // 작성자 학번

    // 생성자
    public DormReviewDTO(Long reviewId, DormDTO dormDTO, String username, String comment, String createDate, Integer studentId) {
        this.reviewId = reviewId;
        this.dormDTO = dormDTO;
        this.username = username;
        this.comment = comment;
        this.createDate = createDate;
        this.studentId = studentId;
    }

    public static DormReviewDTO fromEntity(DormReview dormReview) {
        DormDTO dormDTO = new DormDTO(
                dormReview.getDorm().getDormId(),
                dormReview.getDorm().getDormname()
        );

        return new DormReviewDTO(
                dormReview.getReviewId(),
                dormDTO,
                dormReview.getSiteUser().getUsername(),
                dormReview.getComment(),
                dormReview.getCreateDate().toString(),
                dormReview.getSiteUser().getStudent_Id()
        );
    }
}