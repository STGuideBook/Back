package tools.project.StGuideBook.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DormDTO {
    private final Long dormId;
    private final String dormname;

    public DormDTO(Long dormId, String dormname) {
        this.dormId = dormId;
        this.dormname = dormname;
    }
}
