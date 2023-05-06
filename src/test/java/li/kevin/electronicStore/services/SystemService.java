package li.kevin.electronicStore.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SystemServiceTest {

    SystemService systemService = new SystemService();
    @Test
    void ping() {
        Assertions.assertEquals("pong", systemService.ping());
    }
}
