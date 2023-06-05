package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("상품들을 조회한다.")
    void getProductsTest() {
        var result = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/products")
            .then()
            .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 상품을 조회한다.")
    void getProductTets() {
        var result = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/products/1")
            .then()
            .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        long id = result.body().jsonPath().getLong("id");
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품을 추가한다.")
    public void createProductTest() {
        var product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when()
            .post("/products")
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("추가된 상품을 조회한다.")
    public void getCreatedProduct() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        var location =
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        var responseProduct = given().log().all()
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

    @Test
    @DisplayName("상품 정보를 수정한다.")
    public void updateProductTest() {
        var product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when()
            .put("/products/1")
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        var responseProduct = given().log().all()
            .when()
            .get("/products/1")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getObject(".", ProductResponse.class);

        assertAll(
            () -> assertThat(responseProduct.getId()).isEqualTo(1L),
            () -> assertThat(responseProduct.getName()).isEqualTo("치킨"),
            () -> assertThat(responseProduct.getPrice()).isEqualTo(10000),
            () -> assertThat(responseProduct.getImageUrl()).isEqualTo("http://example.com/chicken.jpg")
        );
    }
}
