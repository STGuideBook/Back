package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(nullable = false)
    private String restaurant_name;
}
