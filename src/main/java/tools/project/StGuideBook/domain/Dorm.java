package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Dorm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dormId;

    @Column(nullable = false)
    private String dormname;

}
