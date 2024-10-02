package tools.project.StGuideBook.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RestaurantReviewDTO {

    private Long RestaurantId;
    private final String comment;
    private final int rating;

    private Long ReviewId;
    private LocalDateTime createDate;
    private String username;
    private String restaurantName;

    @JsonCreator
    public RestaurantReviewDTO(@JsonProperty("comment") String comment,
                               @JsonProperty("rating") int rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public RestaurantReviewDTO(String username, Long RestaurantId, String comment, int rating,
                               LocalDateTime createDate, String restaurantName, Long reviewId) {
        this.username = username;
        this.RestaurantId = RestaurantId;
        this.comment = comment;
        this.rating = rating;
        this.createDate = createDate;
        this.restaurantName = restaurantName;
        this.ReviewId = reviewId;
    }
}
