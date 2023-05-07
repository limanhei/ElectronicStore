package li.kevin.electronicStore.contracts;

import li.kevin.electronicStore.models.Discount;
import li.kevin.electronicStore.fakeRepositories.FakeDiscountRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class DiscountRepositoryContractTest {

    @Autowired
    private DiscountRepository prodAdapter;
    private DiscountRepository fakeAdapter = new FakeDiscountRepository();

    @BeforeEach
    void beforeEach() {
        prodAdapter.deleteAll();
        fakeAdapter.deleteAll();
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to save a discount")
    void saveDiscount(String ignoredTestName, DiscountRepository repository) {
        Discount expected = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        repository.save(expected);
        assertThat(repository.findAll()).contains(expected);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to remove a discount")
    void removeDiscount(String ignoredTestName, DiscountRepository repository) {
        Discount discount = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        discount = repository.save(discount);
        repository.deleteById(discount.getId());
        assertThat(repository.findAll()).doesNotContain(discount);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to update a discount")
    void updateDiscount(String ignoredTestName, DiscountRepository repository) {
        Discount discount = repository.save(new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00")));
        discount.setDiscountedPrice(new BigDecimal("11.00"));
        repository.save(discount);
        assertThat(repository.findById(discount.getId()).get().getDiscountedPrice()).isEqualByComparingTo(new BigDecimal("11.00"));
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find all discounts")
    void findAllDiscounts(String ignoredTestName, DiscountRepository repository) {
        Discount discount1 = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        Discount discount2 = new Discount("some-other-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        repository.save(discount1);
        repository.save(discount2);
        List<Discount> actual = repository.findAll();
        assertThat(actual).contains(discount1);
        assertThat(actual).contains(discount2);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find by name")
    void findById(String ignoredTestName, DiscountRepository repository) {
        Discount discount1 = new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        Discount discount2 = new Discount("some-other-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00"));
        discount1 = repository.save(discount1);
        repository.save(discount2);
        Discount actual = repository.findById(discount1.getId()).get();
        assertThat(actual).isEqualTo(discount1);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to check exist")
    void existsById(String ignoredTestName, DiscountRepository repository) {
        Discount discount = repository.save(new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00")));
        assertThat(repository.existsById(discount.getId())).isTrue();
        assertThat(repository.existsById(9999)).isFalse();
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find by product name")
    void findByProductName(String ignoredTestName, DiscountRepository repository) {
        Discount discount1 = repository.save(new Discount("some-product", 1, new BigDecimal("50.00"), new BigDecimal("10.00")));
        Discount discount2 = repository.save(new Discount("some-product", 2, new BigDecimal("20.00"), new BigDecimal("20.00")));
        List<Discount> discounts = repository.findByProductName("some-product");
        assertThat(discounts).contains(discount1);
        assertThat(discounts).contains(discount2);
    }

    public List<Arguments> adapters() {
        return List.of(
                Arguments.of("prod adapter", prodAdapter),
                Arguments.of("fake adapter", fakeAdapter)
        );
    }
}
