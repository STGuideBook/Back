package tools.project.StGuideBook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.Question;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.AnswerForm;
import tools.project.StGuideBook.service.AnswerService;
import tools.project.StGuideBook.service.QuestionService;
import tools.project.StGuideBook.service.UserService;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/tip_board1")
@RequiredArgsConstructor
@RestController
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public ResponseEntity<?> createAnswer(@PathVariable("id") Integer id,
                                          @Valid @RequestBody AnswerForm answerForm,
                                          BindingResult bindingResult,
                                          Principal principal) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.create(question, answerForm.getContent(), siteUser);
        return ResponseEntity.ok(Map.of("message", "답변이 성공적으로 생성되었습니다.", "questionId", id));
    }
}
