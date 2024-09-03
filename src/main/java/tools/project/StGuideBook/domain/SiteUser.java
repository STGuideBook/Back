package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    public SiteUser(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
    }

    public SiteUser() {}
}
