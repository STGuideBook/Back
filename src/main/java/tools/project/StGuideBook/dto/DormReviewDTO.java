package tools.project.StGuideBook.dto;

import lombok.Getter;
import tools.project.StGuideBook.domain.DormReview;

@Getter
public class DormReviewDTO {

    private String username;
    private String comment;

    public DormReviewDTO(Long reviewId, DormDTO dormDTO, String username, String comment, String string, Integer studentId) {
    }
}
