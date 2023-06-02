package cart.config;

import cart.dao.*;
import cart.fixture.dao.*;
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

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Sql("/truncate.sql")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ControllerTestConfig {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    protected String DOCUMENT_IDENTIFIER = "{method-name}";

    protected MemberDao memberDao;
    protected ProductDao productDao;
    protected CartItemDao cartItemDao;
    protected OrderDao orderDao;
    protected OrderItemDao orderItemDao;

    protected MemberDaoFixture memberDaoFixture;
    protected ProductDaoFixture productDaoFixture;
    protected CartItemDaoFixture cartItemDaoFixture;
    protected OrderDaoFixture orderDaoFixture;
    protected OrderItemDaoFixture orderItemDaoFixture;

    protected RequestSpecification spec;

    @BeforeEach
    void controller(RestDocumentationContextProvider rest) {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);

        memberDaoFixture = new MemberDaoFixture(memberDao);
        productDaoFixture = new ProductDaoFixture(productDao);
        cartItemDaoFixture = new CartItemDaoFixture(cartItemDao);
        orderDaoFixture = new OrderDaoFixture(orderDao);
        orderItemDaoFixture = new OrderItemDaoFixture(orderItemDao);

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
