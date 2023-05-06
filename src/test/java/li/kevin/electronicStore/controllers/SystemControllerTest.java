package li.kevin.electronicStore.controllers;

import li.kevin.electronicStore.services.SystemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(li.kevin.electronicStore.controllers.SystemController.class)
public class SystemControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        SystemService systemService() {
            return new SystemService();
        }
    }
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("should be able to receive response from ping endpoint")
    @Test
    void ping() throws Exception {
        mockMvc.perform(get("/api/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
