package cart.integration;

import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("모든 등록된 상품을 가져온다")
    public void getProducts() {
        var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("새로운 상품을 생성한다")
    public void createProduct() {
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
    @DisplayName("새 상품을 추가하고 해당 상품을 확인한다.")
    public void getCreatedProduct() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // given
        var location =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");

        System.out.println(location);

        // when
        var responseProduct = given().log().all()
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        // then
        assertThat(responseProduct.getId()).isNotNull();
        assertThat(responseProduct.getName()).isEqualTo("피자");
        assertThat(responseProduct.getPrice()).isEqualTo(15_000);
    }

    @Test
    @DisplayName("없는 상품을 조회하면 404 에러가 난다.")
    void Response404WhenGetNotExistProduct() {
        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products/" + 10)
                .then()
                .extract();

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("없는 상품을 수정하면 404 에러가 난다.")
    void Response404WhenUpdateNotExistProduct() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .put("/products/" + 10)
                .then()
                .extract();

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("없는 상품을 삭제하면 404 에러가 난다.")
    void Response404WhenDeleteNotExistProduct() {

        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/products/" + 10)
                .then()
                .extract();

        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
