package li.kevin.electronicStore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import li.kevin.electronicStore.models.*;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import li.kevin.electronicStore.repositories.DiscountRepository;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private BasketItemRepository basketItemRepository;


    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
        discountRepository.deleteAll();
        basketItemRepository.deleteAll();
    }

    @DisplayName("should be able to add a basket item")
    @Test
    void addBasketItem() throws Exception {
        BasketItem expected = new BasketItem("some-client", "some-product", 10);
        String payload = mapper.writeValueAsString(expected);
        mockMvc.perform(post("/client/addBasketItem")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        BasketItem actual = basketItemRepository.findByClientName("some-client").stream().findFirst().orElseThrow();
        assertThat(actual.getQuantity()).isEqualTo(10);
    }

    @DisplayName("should be able to get all basket items")
    @Test
    void getBasket() throws Exception {
        BasketItem basketItem1 = new BasketItem("some-client", "some-product", 10);
        BasketItem basketItem2 = new BasketItem("some-client", "some-other-product", 10);
        basketItemRepository.save(basketItem1);
        basketItemRepository.save(basketItem2);
        String expected = mapper.writeValueAsString(List.of(basketItem1, basketItem2));
        mockMvc.perform(get("/client/basket/some-client"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @DisplayName("should be able to remove a basket item")
    @Test
    void removeBasketItem() throws Exception {
        BasketItem basketItem = new BasketItem("some-client", "some-product", 10);
        basketItem = basketItemRepository.save(basketItem);

        mockMvc.perform(delete("/client/removeBasketItem/" + basketItem.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(basketItem.getId() + " deleted"));
        assertThat(basketItemRepository.findAll()).doesNotContain(basketItem);
    }

    @DisplayName("should return 404 if a basket item to be removed not found")
    @Test
    void removeUnknownBasketItem() throws Exception {
        mockMvc.perform(delete("/client/removeBasketItem/9999"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("should be able to update a basket item")
    @Test
    void updateBasketItem() throws Exception {
        BasketItem expected = basketItemRepository.save(new BasketItem("some-client", "some-product", 10));
        expected.setQuantity(11);
        String payload = mapper.writeValueAsString(expected);
        mockMvc.perform(put("/client/updateBasketItem")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(payload));
        assertThat(basketItemRepository.findById(expected.getId()).orElseThrow().getQuantity()).isEqualTo(11);
    }

    @DisplayName("should return 404 if a basket item to be updated not found")
    @Test
    void updateUnknownBasketItem() throws Exception {
        BasketItem expected = new BasketItem("some-client", "some-product", 10);
        String payload = mapper.writeValueAsString(expected);
        mockMvc.perform(put("/client/updateBasketItem")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("should be able to calculate a receipt for a basket")
    @Test
    void calculateReceiptForBasket() throws Exception {
        Product product1 = productRepository.save(new Product("some-product1", new BigDecimal("10.00")));
        Product product2 = productRepository.save(new Product("some-product2", new BigDecimal("10.00")));
        basketItemRepository.save(new BasketItem("some-client", "some-product1", 10));
        basketItemRepository.save(new BasketItem("some-client", "some-product2", 5));
        Discount discount1 = discountRepository.save(new Discount("some-product1", 2, null, new BigDecimal("2.00")));
        Discount discount2 = discountRepository.save(new Discount("some-product1", 10, null, new BigDecimal("5.00")));
        String expected = mapper.writeValueAsString(new Receipt(
                List.of(new ReceiptItem(product1, 10, Set.of(discount1, discount2), new BigDecimal("85.00")),
                        new ReceiptItem(product2, 5, Set.of(), new BigDecimal("50.00"))
                ), new BigDecimal("135.00")
        ));
        mockMvc.perform(get("/client/calculateReceipt/some-client"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }
}
