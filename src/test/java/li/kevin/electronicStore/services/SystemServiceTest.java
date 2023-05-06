package li.kevin.electronicStore.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SystemServiceTest {

    SystemService systemService = new SystemService();
    @Test
    void ping() {
        Assertions.assertEquals("pong", systemService.ping());
    }
}
