package tools.project.StGuideBook.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "내용은 필수 입력사항 입니다.")
    private String content;
}
