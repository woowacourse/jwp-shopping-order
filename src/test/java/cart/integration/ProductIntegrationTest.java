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
    @DisplayName("상품을 페이지 단위로 조회한다.")
    void getProductsByPage() {
        // given
        final ProductRequest 치킨_등록_요청 = new ProductRequest("치킨", 20_000, "http://example.com/chicken.jpg");
        final ProductRequest 피자_등록_요청 = new ProductRequest("피자", 25_000, "http://example.com/pizza.jpg");
        final ProductRequest 스테이크_등록_요청 = new ProductRequest("스테이크", 50_000, "http://example.com/steak.jpg");
        final ProductRequest 아이스크림_등록_요청 = new ProductRequest("아이스크림", 1_000, "http://example.com/icecream.jpg");
        final ProductRequest 김밥_등록_요청 = new ProductRequest("김밥", 3_000, "http://example.com/gimbap.jpg");
        상품_저장(치킨_등록_요청);
        상품_저장(피자_등록_요청);
        상품_저장(스테이크_등록_요청);
        상품_저장(아이스크림_등록_요청);
        상품_저장(김밥_등록_요청);

        // expected
        /** 첫 번째 페이지 조회 */
        given().log().all()
            .when()
            .get("/products/pages?page=1&size=3")
            .then()
            .body("totalPage", equalTo(2))
            .body("productResponse[0].name", equalTo("치킨"))
            .body("productResponse[0].price", equalTo(20_000))
            .body("productResponse[0].imageUrl", equalTo("http://example.com/chicken.jpg"))
            .body("productResponse[1].name", equalTo("피자"))
            .body("productResponse[1].price", equalTo(25_000))
            .body("productResponse[1].imageUrl", equalTo("http://example.com/pizza.jpg"))
            .body("productResponse[2].name", equalTo("스테이크"))
            .body("productResponse[2].price", equalTo(50_000))
            .body("productResponse[2].imageUrl", equalTo("http://example.com/steak.jpg"));

        /** 두 번째 페이지 조회 */
        given().log().all()
            .when()
            .get("/products/pages?page=2&size=3")
            .then()
            .body("totalPage", equalTo(2))
            .body("productResponse[0].name", equalTo("아이스크림"))
            .body("productResponse[0].price", equalTo(1_000))
            .body("productResponse[0].imageUrl", equalTo("http://example.com/icecream.jpg"))
            .body("productResponse[1].name", equalTo("김밥"))
            .body("productResponse[1].price", equalTo(3_000))
            .body("productResponse[1].imageUrl", equalTo("http://example.com/gimbap.jpg"));
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
