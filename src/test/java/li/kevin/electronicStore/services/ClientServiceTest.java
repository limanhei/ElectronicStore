package li.kevin.electronicStore.services;

import li.kevin.electronicStore.entities.BasketItem;
import li.kevin.electronicStore.fakeRepositories.FakeBasketItemRepository;
import li.kevin.electronicStore.fakeRepositories.FakeDiscountRepository;
import li.kevin.electronicStore.fakeRepositories.FakeProductRepository;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

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
}
