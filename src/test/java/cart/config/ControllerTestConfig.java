package cart.config;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.repository.JdbcCartItemRepository;
import cart.domain.repository.JdbcMemberRepository;
import cart.domain.repository.JdbcProductRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SuppressWarnings("NonAsciiCharacters")
@Sql("/truncate.sql")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ControllerTestConfig {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    protected MemberDao memberDao;
    protected ProductDao productDao;
    protected CartItemDao cartItemDao;
    protected OrderDao orderDao;
    protected JdbcCartItemRepository jdbcCartItemRepository;
    protected JdbcProductRepository jdbcProductRepository;
    protected JdbcMemberRepository jdbcMemberRepository;
    protected RequestSpecification spec;

    @BeforeEach
    void controller(RestDocumentationContextProvider rest) {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        jdbcProductRepository = new JdbcProductRepository(productDao);
        jdbcMemberRepository = new JdbcMemberRepository(memberDao);
        jdbcCartItemRepository = new JdbcCartItemRepository(cartItemDao, jdbcProductRepository, jdbcMemberRepository);
        orderDao = new OrderDao(jdbcTemplate);

        RestAssured.port = port;

        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(rest)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .addFilter(document("{method-name}"))
                .build();
    }
}
