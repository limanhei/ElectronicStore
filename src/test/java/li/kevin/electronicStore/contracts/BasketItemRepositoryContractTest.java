package li.kevin.electronicStore.contracts;

import li.kevin.electronicStore.entities.BasketItem;
import li.kevin.electronicStore.fakeRepositories.FakeBasketItemRepository;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BasketItemRepositoryContractTest {

    @Autowired
    private BasketItemRepository prodAdapter;
    private BasketItemRepository fakeAdapter = new FakeBasketItemRepository();

    @BeforeEach
    void beforeEach() {
        prodAdapter.deleteAll();
        fakeAdapter.deleteAll();
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to save a product")
    void saveBasketItem(String ignoredTestName, BasketItemRepository repository) {
        BasketItem expected = new BasketItem("some-client", "some-product", 10);
        repository.save(expected);
        assertThat(repository.findAll()).contains(expected);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to remove a product")
    void removeBasketItem(String ignoredTestName, BasketItemRepository repository) {
        BasketItem product = new BasketItem("some-client","some-product", 10);
        product = repository.save(product);
        repository.deleteById(product.getId());
        assertThat(repository.findAll()).doesNotContain(product);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to update a basket item")
    void updateBasketItem(String ignoredTestName, BasketItemRepository repository) {
        repository.save(new BasketItem("some-client", "some-product", 10));
        BasketItem basketItem = repository.save(new BasketItem("some-client", "some-product", 11));
        assertThat(repository.findById(basketItem.getId()).orElseThrow().getQuantity()).isEqualTo(11);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find all basket items")
    void findAllBasketItems(String ignoredTestName, BasketItemRepository repository) {
        BasketItem basketItem1 = new BasketItem("some-client","some-product", 10);
        BasketItem basketItem2 = new BasketItem("some-client","some-other-product", 10);
        repository.save(basketItem1);
        repository.save(basketItem2);
        List<BasketItem> actual = repository.findAll();
        assertThat(actual).contains(basketItem1);
        assertThat(actual).contains(basketItem2);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find by id")
    void findById(String ignoredTestName, BasketItemRepository repository) {
        BasketItem basketItem1 = new BasketItem("some-client","some-product", 10);
        BasketItem basketItem2 = new BasketItem("some-client","some-other-product", 10);
        basketItem1 = repository.save(basketItem1);
        repository.save(basketItem2);
        BasketItem actual = repository.findById(basketItem1.getId()).orElseThrow();
        assertThat(actual).isEqualTo(basketItem1);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find by client name")
    void findByClientName(String ignoredTestName, BasketItemRepository repository) {
        BasketItem basketItem1 = new BasketItem("some-client","some-product", 10);
        BasketItem basketItem2 = new BasketItem("some-other-client","some-other-product", 10);
        basketItem1 = repository.save(basketItem1);
        repository.save(basketItem2);
        List<BasketItem> actual = repository.findByClientName("some-client");
        assertThat(actual).contains(basketItem1);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to check exist")
    void existsById(String ignoredTestName, BasketItemRepository repository) {
        BasketItem basketItem = repository.save(new BasketItem("some-client", "some-product", 10));
        assertThat(repository.existsById(basketItem.getId())).isTrue();
        assertThat(repository.existsById(9999)).isFalse();
    }

    public List<Arguments> adapters() {
        return List.of(
                Arguments.of("prod adapter", prodAdapter),
                Arguments.of("fake adapter", fakeAdapter)
        );
    }
}
