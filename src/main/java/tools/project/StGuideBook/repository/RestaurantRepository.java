package tools.project.StGuideBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.project.StGuideBook.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
