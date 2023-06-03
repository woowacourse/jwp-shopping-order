package cart.config;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.product.dao.ProductDao;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@Sql("/reset-data.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class IntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    protected Long productId;
    protected Long productId2;
    protected Member member;
    protected Member member2;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        productId = productDao.getProductById(1L).getId();
        productId2 = productDao.getProductById(2L).getId();

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }
}
