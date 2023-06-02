package cart.integration;

import static io.restassured.RestAssured.given;

import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public Long createProduct(final ProductRequest productRequest) {
        final ExtractableResponse<Response> response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when()
            .post("/products")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        return getIdFromCreatedResponse(response);
    }

    public long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
