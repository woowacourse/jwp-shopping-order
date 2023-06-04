package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.request.ProductCreateRequest;
import cart.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class ProductIntegrationTest extends IntegrationTest {

    @Test
    public void 상품_조회_테스트() {
        final var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ORIGIN, "http://www.example.com")
                .when()
                .get("/products")
                .then().log().all()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 상품_선택_조회_테스트() {
        final String expectedProductIds = "1,3";

        final var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ORIGIN, "http://www.example.com")
                .param("productIds", expectedProductIds)
                .when()
                .get("/products")
                .then().log().all()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.jsonPath().getInt("[0].id")).isEqualTo(1);
        assertThat(result.jsonPath().getInt("[1].id")).isEqualTo(3);
    }

    @Test
    public void 상품_추가_테스트() {
        final var product = new ProductCreateRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        final var response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void 추가된_상품_조회_테스트() {
        final var product = new ProductCreateRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // create product
        final var location =
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");

        // get product
        final var responseProduct = given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        assertThat(responseProduct.getId()).isNotNull();
        assertThat(responseProduct.getName()).isEqualTo("피자");
        assertThat(responseProduct.getPrice()).isEqualTo(15_000);
    }
}
