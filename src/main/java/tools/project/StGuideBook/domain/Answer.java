package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    public Answer(String content, LocalDateTime createDate, Question question, SiteUser author) {
        this.content = content;
        this.createDate = createDate;
        this.question = question;
        this.author = author;
    }

    public Answer() {}
}
