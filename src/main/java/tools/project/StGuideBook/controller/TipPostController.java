package tools.project.StGuideBook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequestMapping("/tip_board")
@RequiredArgsConstructor
@RestController
public class TipPostController {

    private final TipPostService tipPostService;
    private final UserService userService;

    @GetMapping("/list") // 로그인 없이 팁게시판 조회 가능
    public ResponseEntity<List<TipPostDTO>> list() {
        List<TipPost> tipPostList = this.tipPostService.getList();
        List<TipPostDTO> tipPostDTOList = tipPostList.stream()
                .map(this.tipPostService::convertToDto)
                .toList();
        return ResponseEntity.ok(tipPostDTOList);
    }

    @GetMapping(value = "/list/{id}") // 로그인 없이 팁게시판 세부 내용 조회 가능
    public ResponseEntity<TipPostDTO> detail(@PathVariable("id") Integer id) {
        TipPost tipPost = this.tipPostService.getQuestion(id);
        TipPostDTO tipPostDTO = this.tipPostService.convertToDto(tipPost);
        return ResponseEntity.ok(tipPostDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create") // 로그인 해야 팁 작성 가능
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
    @PutMapping("/update/{id}") // 작성자 및 어드민만 팁 수정 가능
    public ResponseEntity<?> update(@PathVariable("id") Integer id,
                                    @Valid @RequestBody TipPostDTO tipPostDTO,
                                    BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        TipPost updatedPost = this.tipPostService.updatePost(id, tipPostDTO, siteUser, LocalDateTime.now());
        return ResponseEntity.ok(this.tipPostService.convertToDto(updatedPost));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}") // 작성자 및 어드민만 팁 삭제 가능
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, Principal principal) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.tipPostService.deletePost(id, siteUser);
        return ResponseEntity.noContent().build();
    }

}
