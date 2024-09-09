package tools.project.StGuideBook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.TipPost;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.TipPostDTO;
import tools.project.StGuideBook.service.TipPostService;
import tools.project.StGuideBook.service.UserService;

import java.security.Principal;
import java.util.List;

@RequestMapping("/tip_board")
@RequiredArgsConstructor
@RestController
public class TipPostController {

    private final TipPostService tipPostService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<TipPostDTO>> list() {
        List<TipPost> tipPostList = this.tipPostService.getList();
        List<TipPostDTO> tipPostDTOList = tipPostList.stream()
                .map(this.tipPostService::convertToDto)
                .toList();
        return ResponseEntity.ok(tipPostDTOList);
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<TipPostDTO> detail(@PathVariable("id") Integer id) {
        TipPost tipPost = this.tipPostService.getQuestion(id);
        TipPostDTO tipPostDTO = this.tipPostService.convertToDto(tipPost);
        return ResponseEntity.ok(tipPostDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody TipPostDTO tipPostDTO,
                                    BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.tipPostService.create(tipPostDTO.getSubject(), tipPostDTO.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
