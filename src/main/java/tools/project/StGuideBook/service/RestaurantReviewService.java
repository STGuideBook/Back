package tools.project.StGuideBook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.UserRole.UserRole;
import tools.project.StGuideBook.domain.Restaurant;
import tools.project.StGuideBook.domain.RestaurantReview;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.dto.RestaurantReviewDTO;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.exception.UnauthorizedException;
import tools.project.StGuideBook.repository.RestaurantRepository;
import tools.project.StGuideBook.repository.RestaurantReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantReviewService {

    private final RestaurantReviewRepository restaurantReviewRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantReviewService(RestaurantReviewRepository restaurantReviewRepository,
                                   RestaurantRepository restaurantRepository) {
        this.restaurantReviewRepository = restaurantReviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantReview addRestaurantReview(Long restaurantId, RestaurantReviewDTO restaurantReviewDTO,
                                                String user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당을 찾을 수 없습니다."));

        RestaurantReview restaurantReview = new RestaurantReview(restaurant, user,
                restaurantReviewDTO.getComment(), restaurantReviewDTO.getRating(), LocalDateTime.now());

        return restaurantReviewRepository.save(restaurantReview);
    }

    public List<RestaurantReviewDTO> getRestaurantReviews(Long restaurantId) {
        List<RestaurantReview> Rest_review = restaurantReviewRepository.findByRestaurantRestaurantId(restaurantId);

        return Rest_review.stream().map(review -> new RestaurantReviewDTO(
                review.getUsername(),
                review.getRestaurant().getRestaurantId(),
                review.getComment(),
                review.getRating(),
                review.getCreateDate(),
                review.getRestaurant().getRestaurant_name(),
                review.getReviewId()
        )).collect(Collectors.toList());
    }

    public void deleteRestaurantReview(Long reviewId, SiteUser user) {
        RestaurantReview restaurantReview = restaurantReviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("리뷰를 찾지 못했습니다."));

        if (!restaurantReview.getUsername().equals(user.getUsername()) && !UserRole.ADMIN.equals(user.getRole())) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        restaurantReviewRepository.delete(restaurantReview);
    }
}
