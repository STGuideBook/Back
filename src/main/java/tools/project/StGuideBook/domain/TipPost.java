package tools.project.StGuideBook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class TipPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "createDate", updatable = false)
    private LocalDateTime createDate;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    @JoinTable(
            name = "tip_post_likes",
            joinColumns = @JoinColumn(name = "tip_post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<SiteUser> likedUsers = new HashSet<>();



    public TipPost(String subject, String content, LocalDateTime createDate, SiteUser author) {
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.author = author;
    }

    public TipPost() {}

    public void toggleLike(SiteUser user) {
        if (likedUsers.contains(user)) {
            likedUsers.remove(user); // 좋아요 취소
        } else {
            likedUsers.add(user); // 좋아요 추가
        }
    }

    public int getLikeCount() {
        return likedUsers.size(); // 좋아요 수 반환
    }
}