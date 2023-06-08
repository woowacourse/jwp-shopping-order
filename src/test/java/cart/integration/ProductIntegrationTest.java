package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductIntegrationTest extends IntegrationTest {

    @DisplayName("상품 목록을 조회한다.")
    @Test
    public void getProducts() {
        final var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("상품을 추가한다.")
    @Test
    public void createProduct() {
        final var product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        final var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("추가한 상품을 조회한다.")
    @Test
    public void getCreatedProduct() {
        final var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // create product
        final var location =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");

        // get product
        final var responseProduct = given().log().all()
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        assertThat(responseProduct.getId()).isNotNull();
        assertThat(responseProduct.getName()).isEqualTo("피자");
        assertThat(responseProduct.getPrice()).isEqualTo(15_000);
    }
}
