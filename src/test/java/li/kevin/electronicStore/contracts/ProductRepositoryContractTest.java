package li.kevin.electronicStore.contracts;

import li.kevin.electronicStore.entities.Product;
import li.kevin.electronicStore.fakeRepositories.FakeProductRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
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
public class ProductRepositoryContractTest {

    @Autowired
    private ProductRepository prodAdapter;
    private ProductRepository fakeAdapter = new FakeProductRepository();

    @BeforeEach
    void beforeEach() {
        prodAdapter.deleteAll();
        fakeAdapter.deleteAll();
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to save a product")
    void saveProduct(String ignoredTestName, ProductRepository repository) {
        Product expected = new Product("some-product", new BigDecimal("10.00"));
        repository.save(expected);
        assertThat(repository.findAll()).contains(expected);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to remove a product")
    void removeProduct(String ignoredTestName, ProductRepository repository) {
        Product product = new Product("some-product", new BigDecimal("10.00"));
        product = repository.save(product);
        repository.deleteById(product.getName());
        assertThat(repository.findAll()).doesNotContain(product);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to update a product")
    void updateProduct(String ignoredTestName, ProductRepository repository) {
        repository.save(new Product("some-product", new BigDecimal("10.00")));
        repository.save(new Product("some-product", new BigDecimal("11.00")));
        assertThat(repository.findById("some-product").get().getPrice()).isEqualByComparingTo(new BigDecimal("11.00"));
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find all products")
    void findAllProducts(String ignoredTestName, ProductRepository repository) {
        Product product1 = new Product("some-product", new BigDecimal("10.00"));
        Product product2 = new Product("some-other-product", new BigDecimal("10.00"));
        repository.save(product1);
        repository.save(product2);
        List<Product> actual = repository.findAll();
        assertThat(actual).contains(product1);
        assertThat(actual).contains(product2);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to find by name")
    void findById(String ignoredTestName, ProductRepository repository) {
        Product product1 = new Product("some-product", new BigDecimal("10.00"));
        Product product2 = new Product("some-other-product", new BigDecimal("10.00"));
        repository.save(product1);
        repository.save(product2);
        Product actual = repository.findById("some-product").get();
        assertThat(actual).isEqualTo(product1);
    }

    @ParameterizedTest(name = "[{index}]: {0}")
    @MethodSource("adapters")
    @DisplayName("Should be able to check exist")
    void existsById(String ignoredTestName, ProductRepository repository) {
        repository.save(new Product("some-product", new BigDecimal("10.00")));
        assertThat(repository.existsById("some-product")).isTrue();
        assertThat(repository.existsById("some-other-product")).isFalse();
    }

    public List<Arguments> adapters() {
        return List.of(
                Arguments.of("prod adapter", prodAdapter),
                Arguments.of("fake adapter", fakeAdapter)
        );
    }
}
