package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.RestaurantReview;

import java.util.List;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
    List<RestaurantReview> findByRestaurantRestaurantId(Long restaurantId);
}
