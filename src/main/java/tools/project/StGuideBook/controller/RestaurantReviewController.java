package tools.project.StGuideBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tools.project.StGuideBook.domain.RestaurantReview;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.RestaurantReviewDTO;
import tools.project.StGuideBook.service.RestaurantReviewService;
import tools.project.StGuideBook.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/restaurant_review")
public class RestaurantReviewController {

    private final RestaurantReviewService restaurantReviewService;
    private final UserService userService;

    @Autowired
    public RestaurantReviewController(RestaurantReviewService restaurantReviewService, UserService userService) {
        this.restaurantReviewService = restaurantReviewService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add_review/{restaurantId}")
    public ResponseEntity<RestaurantReview> addRestaurantReview(@PathVariable("restaurantId") Long restaurantId,
                                                                @RequestParam(name = "username") String username,
                                                                @RequestBody RestaurantReviewDTO restaurantReviewDTO) {
        try {
            RestaurantReview newReview = restaurantReviewService.addRestaurantReview(restaurantId,
                    restaurantReviewDTO, username);
            return new ResponseEntity<>(newReview, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review_list")
    public ResponseEntity<List<RestaurantReviewDTO>> getAllRestaurantReviews(@RequestParam(name = "restaurantId") Long reviewId) {
        
        try {
            List<RestaurantReviewDTO> restReviews = restaurantReviewService.getRestaurantReviews(reviewId);
            if (restReviews.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(restReviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete_review") // 작성자 및 어드민만 리뷰 삭제 가능
    public ResponseEntity<?> deleteDormReview(@RequestParam(name = "reviewId") Long reviewId,
                                              @RequestParam(name = "username") String username) {

        SiteUser siteUser = this.userService.getUser(username);
        this.restaurantReviewService.deleteRestaurantReview(reviewId, siteUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
