package li.kevin.electronicStore.services;

import li.kevin.electronicStore.models.Discount;
import li.kevin.electronicStore.models.Product;
import li.kevin.electronicStore.fakeRepositories.FakeDiscountRepository;
import li.kevin.electronicStore.fakeRepositories.FakeProductRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdminServiceTest {

    ProductRepository productRepository = new FakeProductRepository();
    DiscountRepository discountRepository = new FakeDiscountRepository();
    AdminService adminService = new AdminService(productRepository, discountRepository);
    @Test
    @DisplayName("Should be able to save a product")
    void saveProduct() {
        Product expected = new Product("some-product", new BigDecimal("10.00"));
        adminService.saveProduct(expected);
        assertThat(productRepository.findAll()).contains(expected);
    }

    @Test
    @DisplayName("Should be able to get all products")
    void getAllProducts() {
        Product expected = new Product("some-product", new BigDecimal("10.00"));
        adminService.saveProduct(expected);
        assertThat(adminService.getProducts()).contains(expected);
    }

    @Test
    @DisplayName("Should be able to remove a product")
    void removeProduct() {
        Product expected = new Product("some-product", new BigDecimal("10.00"));
        adminService.saveProduct(expected);
        adminService.removeProduct(expected.getName());
        assertThat(productRepository.findAll()).doesNotContain(expected);
    }

    @Test
    @DisplayName("Should throw exception if product does not exist when remove a product")
    void removeProductDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.removeProduct("some-product"));
        String expectedMessage = "404 NOT_FOUND \"some-product not found!\"";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Should be able to update a product")
    void updateProduct() {
        Product expected = new Product("some-product", new BigDecimal("10.00"));
        adminService.saveProduct(expected);
        adminService.updateProduct(new Product("some-product", new BigDecimal("11.00")));
        assertThat(productRepository.findById("some-product").orElseThrow().getPrice())
                .isEqualByComparingTo(new BigDecimal("11.00"));
    }

    @Test
    @DisplayName("Should throw exception if product does not exist when remove a product")
    void updateProductDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.updateProduct(new Product("some-product", new BigDecimal("10.00"))));
        String expectedMessage = "404 NOT_FOUND \"some-product not found!\"";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Should be able to save a discount")
    void saveDiscount() {
        adminService.saveProduct(new Product("some-product", new BigDecimal("10.00")));
        Discount expected = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("5.00"));
        adminService.saveDiscount(expected);

        assertThat(discountRepository.findAll()).contains(expected);
    }

    @Test
    @DisplayName("Should be able to get all discounts")
    void getAllDiscounts() {
        adminService.saveProduct(new Product("some-product", new BigDecimal("10.00")));
        Discount expected = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("5.00"));
        adminService.saveDiscount(expected);

        assertThat(adminService.getDiscounts()).contains(expected);
    }

    @Test
    @DisplayName("Should throw exception if product does not exist when adding a discount")
    void addDiscountWhenProductDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.saveDiscount(new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("5.00"))));
        String expectedMessage = "400 BAD_REQUEST \"Product some-product not found!\"";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Should be able to remove a discount")
    void removeDiscount() {
        adminService.saveProduct(new Product("some-product", new BigDecimal("10.00")));
        Discount expected = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        adminService.saveDiscount(expected);
        adminService.removeDiscount(expected.getId());
        assertThat(discountRepository.findAll()).doesNotContain(expected);
    }

    @Test
    @DisplayName("Should throw exception if discount does not exist when remove a discount")
    void removeDiscountDoesNotExist() {
        adminService.saveProduct(new Product("some-product", new BigDecimal("10.00")));
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.removeDiscount(9999));
        String expectedMessage = "404 NOT_FOUND \"Discount 9999 not found!\"";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Should be able to update a discount")
    void updateDiscount() {
        adminService.saveProduct(new Product("some-product", new BigDecimal("10.00")));
        Discount expected = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        expected = adminService.saveDiscount(expected);
        expected.setDiscountedPercent(new BigDecimal("60.00"));
        adminService.updateDiscount(expected);
        assertThat(discountRepository.findById(expected.getId()).orElseThrow().getDiscountedPercent())
                .isEqualByComparingTo(new BigDecimal("60.00"));
    }

    @Test
    @DisplayName("Should throw exception if discount does not exist when remove a discount")
    void updateDiscountDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> adminService.updateDiscount(new Discount("some-product", 1, new BigDecimal("50.00"),new BigDecimal("10.00"))));
        String actualMessage = exception.getMessage();

        MatcherAssert.assertThat(actualMessage, matchesPattern("404 NOT_FOUND \"Discount \\d+ not found!\""));
    }
}
