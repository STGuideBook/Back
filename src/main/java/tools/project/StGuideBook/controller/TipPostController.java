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

    @GetMapping(value = "/list/{id}")
    public ResponseEntity<TipPostDTO> detail(@PathVariable("id") Integer id) {
        TipPost tipPost = this.tipPostService.getQuestion(id);
        TipPostDTO tipPostDTO = this.tipPostService.convertToDto(tipPost);
        return ResponseEntity.ok(tipPostDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody TipPostDTO tipPostDTO,
                                    BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.tipPostService.create(tipPostDTO.getSubject(), tipPostDTO.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{author_id}")
    public ResponseEntity<?> update(@PathVariable("author_id") Integer author_id,
                                    @Valid @RequestBody TipPostDTO tipPostDTO,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        TipPost updatedPost = this.tipPostService.updatePost(author_id, tipPostDTO);
        return ResponseEntity.ok(this.tipPostService.convertToDto(updatedPost));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{author_id}")
    public ResponseEntity<?> delete(@PathVariable("author_id") Integer author_id) {
        this.tipPostService.deletePost(author_id);
        return ResponseEntity.noContent().build();
    }

}
