package cart.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private JdbcTemplate jdbctemplate;

    private List<String> tableNames = List.of(
            "order_detail",
            "orders",
            "cart_item",
            "point",
            "product",
            "member"
    );

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void clear() {
        clearTables();
    }

    void clearTables() {
        for (String tableName : tableNames) {
            jdbctemplate.execute("DELETE FROM " + tableName);
        }
    }
}
