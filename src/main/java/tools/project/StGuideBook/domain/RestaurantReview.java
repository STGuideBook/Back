package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class RestaurantReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    private int rating;

    private LocalDateTime createDate;

    public RestaurantReview(Restaurant restaurant,  String username, String comment,
                            int rating, LocalDateTime createDate) {
        this.restaurant = restaurant;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.createDate = createDate;
    }

    public RestaurantReview() {

    }

}
