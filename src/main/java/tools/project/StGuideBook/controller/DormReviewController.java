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

import java.security.Principal;
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
    @PostMapping("/add_review/{dormId}")
    public ResponseEntity<DormReviewDTO> addDormReview(@PathVariable("dormId") Long dormId,
                                                       @RequestBody DormReviewDTO dormReviewDTO) {
        try {
            DormReview newReview = dormReviewService.addDormReview(dormId, dormReviewDTO.getUsername(), dormReviewDTO.getComment());
            DormReviewDTO responseDto = dormReviewService.convertToDTO(newReview);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review_list")
    public ResponseEntity<List<DormReviewDTO>> getDormReview(@RequestParam(name = "dormId") Long dormId) {
        try {
            List<DormReview> dormReviews = dormReviewService.getReviewsByDorm(dormId);

            if (dormReviews.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 리뷰가 없는 경우
            }

            List<DormReviewDTO> responseDto = dormReviews.stream()
                    .map(dormReviewService::convertToDTO)
                    .toList();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 기숙사가 없는 경우
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete_review") // 작성자 및 어드민만 리뷰 삭제 가능
    public ResponseEntity<?> deleteDormReview(@RequestParam(name = "reviewId") Long reviewId,
                                              Principal principal) {

        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.dormReviewService.deleteDormReview(reviewId, siteUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
