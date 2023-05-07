package li.kevin.electronicStore.repositories;

import li.kevin.electronicStore.models.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketItemRepository extends JpaRepository<BasketItem, Integer> {
    List<BasketItem> findByClientName(String clientName);
}
