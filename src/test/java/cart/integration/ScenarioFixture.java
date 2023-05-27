package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class ScenarioFixture extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;

    protected Member 사용자1;
    protected Member 사용자2;
    protected Long 상품아이디1;
    protected Long 상품아이디2;

    @BeforeEach
    void setUp() {
        super.setUp();

        상품아이디1 = 새_상품을_만든다(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        상품아이디2 = 새_상품을_만든다(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        사용자1 = memberDao.getMemberById(1L);
    }

    private Long 새_상품을_만든다(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return 상품_아이디를_가져온다(response);
    }

    private long 상품_아이디를_가져온다(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
