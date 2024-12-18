package tools.project.StGuideBook.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DormDTO {

    @NotNull
    private final Long dormId;

    @NotNull
    private final String dormname;
}
