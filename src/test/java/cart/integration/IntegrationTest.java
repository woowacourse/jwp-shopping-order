package cart.integration;

import common.DatabaseSetting;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(DatabaseSetting.class)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    public DatabaseSetting databaseSetting;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
}
