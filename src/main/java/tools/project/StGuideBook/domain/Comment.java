package tools.project.StGuideBook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    @JsonBackReference
    private TipPost tipPost;

    @ManyToOne
    private SiteUser author;

    public Comment(String content, LocalDateTime createDate, TipPost tipPost, SiteUser author) {
        this.content = content;
        this.createDate = createDate;
        this.tipPost = tipPost;
        this.author = author;
    }

    public Comment() {}
}
