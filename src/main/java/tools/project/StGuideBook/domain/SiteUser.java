package tools.project.StGuideBook.domain;

import jakarta.persistence.*;
import lombok.Getter;
import tools.project.StGuideBook.UserRole.UserRole;

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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public SiteUser(String username, String email, String encode, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = encode;
        this.role = role;
    }

    public SiteUser() {}
}
