package com.vzdolci.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/test",
    "spring.datasource.username=test",
    "spring.datasource.password=test",
    "spring.flyway.enabled=false"
})
class VzDolciBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
