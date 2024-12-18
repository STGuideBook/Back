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
    @JoinColumn(name = "dorm_id", nullable = false)
    private Dorm dorm;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "site_user_id", nullable = false)
    private SiteUser siteUser;

    private LocalDateTime createDate;

    public DormReview(Dorm dorm, SiteUser siteUser, LocalDateTime createDate, String comment) {
        this.dorm = dorm;
        this.siteUser = siteUser;
        this.createDate = createDate;
        this.comment = comment;
    }

    public DormReview() {

    }
}

