package tools.project.StGuideBook.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
    private String student_Id;
}
