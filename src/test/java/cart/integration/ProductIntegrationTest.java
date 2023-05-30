package cart.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import cart.application.dto.product.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("상품 리스트를 조회한다.")
    public void getProducts() {
        // given
        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        final ProductRequest 피자_등록_요청 = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");
        상품_저장(치킨_등록_요청);
        상품_저장(피자_등록_요청);

        // expected
        given().log().all()
            .when()
            .get("/products")
            .then()
            .body("size", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].name", equalTo("치킨"))
            .body("[0].price", equalTo(10000))
            .body("[0].imageUrl", equalTo("http://example.com/chicken.jpg"))
            .body("[1].id", equalTo(2))
            .body("[1].name", equalTo("피자"))
            .body("[1].price", equalTo(15000))
            .body("[1].imageUrl", equalTo("http://example.com/pizza.jpg"));
    }

    @Test
    @DisplayName("상품을 추가한다.")
    public void createProduct() {
        // given
        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        // expected
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(치킨_등록_요청)
            .when()
            .post("/products")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header(LOCATION, "/products/" + 1);
    }

    @Test
    @DisplayName("특정 상품을 조회한다.")
    public void getCreatedProduct() {
        // given
        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        // expected
        given().log().all()
            .when()
            .get("/products/{id}", 1)
            .then()
            .body("id", equalTo(1))
            .body("name", equalTo("치킨"))
            .body("price", equalTo(10000))
            .body("imageUrl", equalTo("http://example.com/chicken.jpg"));
    }

    @Test
    @DisplayName("상품 정보를 업데이트한다.")
    void updateProduct() {
        // given
        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        // when
        final ProductRequest 치킨_수정_요청 = new ProductRequest("치킨", 20_000, "http://example.com/new-chicken.jpg");
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(치킨_수정_요청)
            .when()
            .put("/products/{id}", 1)
            .then()
            .statusCode(HttpStatus.OK.value());

        // then
        given().log().all()
            .when()
            .get("/products/{id}", 1)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("name", equalTo("치킨"))
            .body("price", equalTo(20000))
            .body("imageUrl", equalTo("http://example.com/new-chicken.jpg"));
    }

    @Test
    @DisplayName("상품 정보를 삭제한다.")
    void deleteProduct() {
        // given
        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_저장(치킨_등록_요청);

        // when
        given().log().all()
            .when()
            .delete("/products/{id}", 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        given().log().all()
            .when()
            .get("/products")
            .then()
            .body("size", is(0));
    }
}
