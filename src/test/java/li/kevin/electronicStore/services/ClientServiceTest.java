package li.kevin.electronicStore.services;

import li.kevin.electronicStore.models.*;
import li.kevin.electronicStore.fakeRepositories.FakeBasketItemRepository;
import li.kevin.electronicStore.fakeRepositories.FakeDiscountRepository;
import li.kevin.electronicStore.fakeRepositories.FakeProductRepository;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientServiceTest {

    ProductRepository productRepository = new FakeProductRepository();
    DiscountRepository discountRepository = new FakeDiscountRepository();

    BasketItemRepository basketItemRepository = new FakeBasketItemRepository();
    ClientService clientService = new ClientService(productRepository, discountRepository, basketItemRepository);
    @Test
    @DisplayName("Should be able to save a basket item")
    void saveBasketItem() {
        BasketItem expected = new BasketItem("some-client", "some-product", 10);
        clientService.saveBasketItem(expected);
        assertThat(basketItemRepository.findAll()).contains(expected);
    }

    @Test
    @DisplayName("Should be able to get basket by client name")
    void getBasket() {
        BasketItem expected = new BasketItem("some-client", "some-product", 10);
        clientService.saveBasketItem(expected);
        assertThat(clientService.getBasket("some-client")).contains(expected);
    }

    @Test
    @DisplayName("Should be able to remove a basket item")
    void removeBasketItem() {
        BasketItem expected = new BasketItem("some-client", "some-product", 10);
        expected = clientService.saveBasketItem(expected);
        clientService.removeBasketItem(expected.getId());
        assertThat(basketItemRepository.findAll()).doesNotContain(expected);
    }

    @Test
    @DisplayName("Should throw exception if basket item does not exist when remove a product")
    void removeBasketItemDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> clientService.removeBasketItem(9999));
        String expectedMessage = "404 NOT_FOUND \"9999 not found!\"";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Should be able to update a basket item")
    void updateBasketItem() {
        BasketItem basketItem = new BasketItem("some-client", "some-product", 10);
        basketItem.setQuantity(11);
        basketItem = clientService.saveBasketItem(basketItem);
        clientService.updateBasketItem(basketItem);
        assertThat(basketItemRepository.findById(basketItem.getId()).orElseThrow().getQuantity()).isEqualTo(11);
    }

    @Test
    @DisplayName("Should throw exception if basket item does not exist when remove a product")
    void updateBasketItemDoesNotExist() {
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> clientService.updateBasketItem(new BasketItem("some-client", "some-product", 10)));
        String expectedMessage = "404 NOT_FOUND \"0 not found!\"";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket")
    void calculateReceipt() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 1));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 5));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 1, Set.of(), new BigDecimal("10.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("60.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket with discount in percent")
    void calculateReceiptWithDiscountByPercent() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 1));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 5));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 1, new BigDecimal("50.00"), null));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 1, Set.of(discount1), new BigDecimal("5.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("55.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket with discount in price")
    void calculateReceiptWithDiscountByPrice() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 1));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 5));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 1, null, new BigDecimal("2.00")));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 1, Set.of(discount1), new BigDecimal("8.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("58.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket for multiple times discount in percent with qualified quantity")
    void calculateReceiptWithDiscountMultipleTimesDiscountsInPercent() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 10));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 5));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 2, new BigDecimal("60.00"), null));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 10, Set.of(discount1), new BigDecimal("80.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("130.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket for multiple times discount in price with qualified quantity")
    void calculateReceiptWithDiscountMultipleTimesDiscountsInPrice() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 10));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 5));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 2, null, new BigDecimal("2.00")));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 10, Set.of(discount1), new BigDecimal("90.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("140.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket for multiple discount on different products")
    void calculateReceiptWithDiscountMultipleDiscountsOnDifferentProducts() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 10));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 7));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 2, new BigDecimal("60.00"), null));
        Discount discount2 = discountRepository.save(new Discount("some-product2", 3, null, new BigDecimal("1.00")));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 10, Set.of(discount1), new BigDecimal("80.00")),
                        new ReceiptItem(product2, 7, Set.of(discount2), new BigDecimal("68.00"))
                ), new BigDecimal("148.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a basket for multiple times discounts on the same product")
    void calculateReceiptWithDiscountMultipleTimesDiscountsOnTheSameProduct() {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product1", 10));
        clientService.saveBasketItem( new BasketItem("some-client", "some-product2", 5));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 2, null, new BigDecimal("2.00")));
        Discount discount2 = discountRepository.save(new Discount("some-product1", 10, null, new BigDecimal("5.00")));
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(
                List.of(new ReceiptItem(product1, 10, Set.of(discount1, discount2), new BigDecimal("85.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("135.00")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should be able to calculate a receipt for a empty basket")
    void calculateReceiptEmptyBasket() {
        Receipt actual = clientService.calculateReceipt("some-client");
        Receipt expected = new Receipt(List.of(), new BigDecimal("0.00"));
        assertThat(actual).isEqualTo(expected);
    }
}
