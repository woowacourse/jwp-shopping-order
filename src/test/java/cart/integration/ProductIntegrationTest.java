package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("등록된 전체 상품 목록을 조회한다.")
    public void findAllProducts() {

        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();

        var products = response.jsonPath().getList(".", ProductResponse.class);

        assertThat(products).extracting(ProductResponse::getName).containsExactlyInAnyOrder("피자", "치킨", "샐러드");
    }

    @Test
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
    public void findCreatedProduct() {
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
}
