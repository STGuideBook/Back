package tools.project.StGuideBook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.Question;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.form.QuestionForm;
import tools.project.StGuideBook.service.QuestionService;
import tools.project.StGuideBook.service.UserService;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<Question>> list() {
        List<Question> questionList = this.questionService.getList();
        return ResponseEntity.ok(questionList);
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<Question> detail(@PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        return ResponseEntity.ok(question);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody QuestionForm questionForm,
                                    BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
