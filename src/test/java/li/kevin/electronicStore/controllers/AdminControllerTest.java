package li.kevin.electronicStore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import li.kevin.electronicStore.entities.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        productRepository.deleteAll();
    }

    @DisplayName("should be able to add a product")
    @Test
    void addProduct() throws Exception {
        Product expected = new Product("some-product", new BigDecimal("10.0"));
        String payload = mapper.writeValueAsString(expected);
        mockMvc.perform(post("/admin/addProduct")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(payload));
        assertThat(productRepository.findAll()).contains(expected);
    }

    @DisplayName("should be able to get all products")
    @Test
    void getProducts() throws Exception {
        Product product1 = new Product("some-product", new BigDecimal("10.00"));
        Product product2 = new Product("some-other-product", new BigDecimal("10.00"));
        productRepository.save(product1);
        productRepository.save(product2);
        String expected = mapper.writeValueAsString(List.of(product1, product2));
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @DisplayName("should be able to remove a product")
    @Test
    void removeProduct() throws Exception {
        Product product = new Product("some-product", new BigDecimal("10.0"));
        productRepository.save(product);

        mockMvc.perform(delete("/admin/removeProduct/some-product"))
                .andExpect(status().isOk())
                .andExpect(content().string("some-product deleted"));
        assertThat(productRepository.findAll()).doesNotContain(product);
    }

    @DisplayName("should return 404 if a product to be removed not found")
    @Test
    void removeUnknownProduct() throws Exception {
        mockMvc.perform(delete("/admin/removeProduct/some-product"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("should be able to update a product")
    @Test
    void updateProduct() throws Exception {
        productRepository.save(new Product("some-product", new BigDecimal("10.0")));

        Product expected = new Product("some-product", new BigDecimal("11.0"));
        String payload = mapper.writeValueAsString(expected);
        mockMvc.perform(put("/admin/updateProduct")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(payload));
        assertThat(productRepository.findById("some-product").get().getPrice()).isEqualByComparingTo(new BigDecimal("11.0"));
    }

    @DisplayName("should return 404 if a product to be updated not found")
    @Test
    void updateUnknownProduct() throws Exception {
        Product expected = new Product("some-product", new BigDecimal("10.0"));
        String payload = mapper.writeValueAsString(expected);
        mockMvc.perform(put("/admin/updateProduct")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
