package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class DormReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "dorm_id")
    private Dorm dorm;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String comment;

    private LocalDateTime createDate;

    public DormReview(Dorm dorm, String username, LocalDateTime createDate,String comment) {
        this.dorm = dorm;
        this.username = username;
        this.createDate = createDate;
        this.comment = comment;
    }

    public DormReview() {

    }
}

