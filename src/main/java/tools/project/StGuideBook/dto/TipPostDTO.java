package tools.project.StGuideBook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TipPostDTO {

    @NotEmpty(message = "제목은 필수 입력사항 입니다.")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수 입력사항 입니다.")
    private String content;

    private LocalDateTime createDate;

    private int likeCount;

    public TipPostDTO(String subject, String content, LocalDateTime createDate, int likeCount) {
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.likeCount = likeCount;
    }

    public TipPostDTO() {}
}