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
import tools.project.StGuideBook.dto.TipPostForm;
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
    public ResponseEntity<List<TipPost>> list() {
        List<TipPost> tipPostList = this.tipPostService.getList();
        return ResponseEntity.ok(tipPostList);
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<TipPost> detail(@PathVariable("id") Integer id) {
        TipPost tipPost = this.tipPostService.getQuestion(id);
        return ResponseEntity.ok(tipPost);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody TipPostForm tipPostForm,
                                    BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.tipPostService.create(tipPostForm.getSubject(), tipPostForm.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
