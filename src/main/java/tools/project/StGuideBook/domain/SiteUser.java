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

    private Integer student_Id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public SiteUser(String username, String encode, Integer student_Id, UserRole role) {
        this.username = username;
        this.password = encode;
        this.student_Id = student_Id;
        this.role = role;
    }

    public SiteUser() {}

    public void changeRoleToAdmin() {
        this.role = UserRole.ADMIN;
    }

    public void changeRoleToUser() {
        this.role = UserRole.USER;
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
