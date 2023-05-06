package li.kevin.electronicStore.services;

import li.kevin.electronicStore.entities.Product;
import li.kevin.electronicStore.fakeRepositories.FakeProductRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminServiceTest {

    ProductRepository repository = new FakeProductRepository();
    AdminService adminService = new AdminService(repository);
    @Test
    @DisplayName("Should be able to save product")
    void saveProduct() {
        Product expected = new Product("some-product", new BigDecimal("10.0"));
        adminService.saveProduct(expected);
        assertThat(repository.findAll()).contains(expected);
    }

    @Test
    @DisplayName("Should be able to remove product")
    void removeProduct() {
        Product expected = new Product("some-product", new BigDecimal("10.0"));
        adminService.saveProduct(expected);
        adminService.removeProduct(expected.getName());
        assertThat(repository.findAll()).doesNotContain(expected);
    }

    @Test
    @DisplayName("Should be able to throw exception if product does not exist when remove product")
    void removeProductDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.removeProduct("some-product"));
        String expectedMessage = "some-product not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Should be able to update product")
    void updateProduct() {
        Product expected = new Product("some-product", new BigDecimal("10.0"));
        adminService.saveProduct(expected);
        adminService.updateProduct(new Product("some-product", new BigDecimal("11.0")));
        assertThat(repository.findById("some-product").get().getPrice())
                .isEqualByComparingTo(new BigDecimal("11.0"));
    }

    @Test
    @DisplayName("Should be able to throw exception if product does not exist when remove product")
    void updateProductDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.updateProduct(new Product("some-product", new BigDecimal("10.0"))));
        String expectedMessage = "some-product not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
