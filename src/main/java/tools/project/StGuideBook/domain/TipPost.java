package tools.project.StGuideBook.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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

    @OneToMany(mappedBy = "tipPost", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Comment> commentList;

    @ManyToOne
    private SiteUser author;

    public TipPost(String subject, String content, LocalDateTime createDate, SiteUser author) {
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.author = author;
    }

    public TipPost() {}
}
