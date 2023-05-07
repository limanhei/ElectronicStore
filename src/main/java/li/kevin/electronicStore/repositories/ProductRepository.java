package li.kevin.electronicStore.repositories;

import li.kevin.electronicStore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
