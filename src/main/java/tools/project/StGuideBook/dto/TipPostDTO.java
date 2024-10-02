package tools.project.StGuideBook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TipPostDTO {

    @NotEmpty(message = "제목은 필수 입력사항 입니다.")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수 입력사항 입니다.")
    private String content;

    private LocalDateTime createDate;

    public TipPostDTO(String subject, String content, LocalDateTime createDate) {
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
    }

    public TipPostDTO() {}
}
