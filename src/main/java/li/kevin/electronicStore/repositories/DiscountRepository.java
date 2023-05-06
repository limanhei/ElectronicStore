package li.kevin.electronicStore.repositories;

import li.kevin.electronicStore.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByProductName(String productName);
}
