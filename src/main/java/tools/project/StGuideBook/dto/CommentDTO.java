package tools.project.StGuideBook.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    @NotEmpty(message = "내용은 필수 입력사항 입니다.")
    private String content;

    public CommentDTO(String content) {
        this.content = content;
    }

    public CommentDTO() {}
}
