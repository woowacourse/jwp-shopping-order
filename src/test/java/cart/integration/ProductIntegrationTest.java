package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    public void 모든_상품을_가져온다() {
        var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 상품을_생성한다() {
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
    public void 생성된_상품을_가져온다() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // create product
        var location =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");

        // get product
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
    public void 없는_상품을_조회하면_404_에러가_반환된다() {
        // get product
        var responseProduct = given().log().all()
                .when()
                .get("/products/" + Long.MAX_VALUE)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void 없는_상품을_수정하면_404_에러가_반환된다() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        var responseProduct = given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .put("/products/" + Long.MAX_VALUE)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void 없는_상품을_삭제하면_404_에러가_반환된다() {
        var responseProduct = given().log().all()
                .when()
                .delete("/products/" + Long.MAX_VALUE)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
