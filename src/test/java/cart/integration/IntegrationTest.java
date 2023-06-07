package cart.integration;

import cart.dao.ProductDao;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
}
