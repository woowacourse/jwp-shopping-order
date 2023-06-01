package cart.config;

import cart.domain.repository.CartItemRepository;
import cart.domain.repository.MemberRepository;
import cart.domain.repository.ProductRepository;
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

    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected CartItemRepository cartItemRepository;
    protected RequestSpecification spec;

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void controller(RestDocumentationContextProvider rest) {
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
