package tools.project.StGuideBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.DormReview;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.DormReviewDTO;
import tools.project.StGuideBook.service.DormReviewService;
import tools.project.StGuideBook.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/dorm_reviews")
public class DormReviewController {

    private final DormReviewService dormReviewService;
    private final UserService userService;

    @Autowired
    public DormReviewController(DormReviewService dormReviewService, UserService userService) {
        this.dormReviewService = dormReviewService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add_review/{dormId}") // 로그인 해야 리뷰 작성 가능
    public ResponseEntity<DormReview> addDormReview(@PathVariable("dormId") Long dormId,
                                                    @RequestParam(name = "username") String username,
                                                    @RequestBody DormReviewDTO dormReviewDTO) {
        try {
            DormReview newReview = dormReviewService.addDormReview(dormId, username, dormReviewDTO.getComment());
            return new ResponseEntity<>(newReview, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review_list") // 로그인 없이 리뷰 조회 가능
    public ResponseEntity<List<DormReview>> getDormReview(@RequestParam(name = "dormId") Long dormId) {

        try {
            List<DormReview> dormReviews = dormReviewService.getReviewsByDorm(dormId);
            if (dormReviews.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(dormReviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete_review") // 작성자 및 어드민만 리뷰 삭제 가능
    public ResponseEntity<?> deleteDormReview(@RequestParam(name = "reviewId") Long reviewId,
                                              @RequestParam(name = "username") String username) {

        SiteUser siteUser = this.userService.getUser(username);
        this.dormReviewService.deleteDormReview(reviewId, siteUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
