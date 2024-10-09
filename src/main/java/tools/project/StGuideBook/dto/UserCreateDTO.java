package tools.project.StGuideBook.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목 입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목 입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목 입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목 입니다.")
    @Email
    private String email;

    @Min(value = 1, message = "학생 ID는 1 이상이어야 합니다.")
    @NotNull(message = "학번은 필수항목 입니다.")
    private Integer student_Id;

}
