package li.kevin.electronicStore.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SystemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("should be able to receive response from ping endpoint")
    @Test
    void ping() throws Exception {
        mockMvc.perform(get("/system/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
