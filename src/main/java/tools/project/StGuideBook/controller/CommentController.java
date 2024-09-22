package tools.project.StGuideBook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.TipPost;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.CommentDTO;
import tools.project.StGuideBook.service.CommentService;
import tools.project.StGuideBook.service.TipPostService;
import tools.project.StGuideBook.service.UserService;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/tip_board")
@RequiredArgsConstructor
@RestController
public class CommentController { // 모든 팁게시판 기능에 대한 컨트롤러

    private final TipPostService tipPostService;
    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public ResponseEntity<?> createComment(@PathVariable("id") Integer id,
                                           @Valid @RequestBody CommentDTO commentDTO,
                                           BindingResult bindingResult,
                                           Principal principal) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        TipPost tipPost = this.tipPostService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.commentService.create(tipPost, commentDTO.getContent(), siteUser);
        return ResponseEntity.ok(Map.of("message", "답변이 성공적으로 생성되었습니다.", "questionId", id));
    }
}
