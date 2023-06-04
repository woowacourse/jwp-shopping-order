package cart.integration;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.exception.ErrorCode.INVALID_PRODUCT_ID;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 API 테스트")
public class ProductIntegrationTest extends IntegrationTest {

    private ProductRequest chicken;
    private ProductRequest pizza;

    @BeforeEach
    void setUp() {
        super.setUp();
        chicken = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        pizza = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");
    }

    @DisplayName("상품을 정상적으로 생성한다.")
    @Test
    public void createProduct() {
        // then
        var response = 상품_생성_요청(chicken);

        // when
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    public void getAllProducts() {
        // given
        상품_생성_요청(chicken);
        상품_생성_요청(pizza);

        // when
        var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        // then
        List<ProductResponse> productResponses = result.jsonPath().getList(".", ProductResponse.class);
        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productResponses.size()).isEqualTo(2),
                () -> assertThat(productResponses.get(0))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(ProductResponse.of(new Product(chicken.getName(), chicken.getPrice(), chicken.getImageUrl()))),
                () -> assertThat(productResponses.get(1))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(ProductResponse.of(new Product(pizza.getName(), pizza.getPrice(), pizza.getImageUrl())))
        );
    }

    @DisplayName("상품을 정상적으로 생성 후 해당 상품을 조회한다.")
    @Test
    public void getProductById() {
        // given
        var location = 상품_생성_요청(pizza).header("Location");

        // when
        var responseProduct = 단일_상품_조회(location)
                .jsonPath()
                .getObject(".", ProductResponse.class);
        ;

        // then
        assertAll(
                () -> assertThat(responseProduct.getId()).isNotNull(),
                () -> assertThat(responseProduct.getName()).isEqualTo(pizza.getName()),
                () -> assertThat(responseProduct.getPrice()).isEqualTo(pizza.getPrice())
        );
    }

    @DisplayName("상품을 업데이트 한다.")
    @Test
    void updateProduct() {
        // given
        var location = 상품_생성_요청(chicken).header("location");

        // when
        // 피자로 상품 업데이트
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pizza)
                .when()
                .put(location)
                .then()
                .extract();

        ProductResponse responseProduct = 단일_상품_조회(location)
                .jsonPath()
                .getObject(".", ProductResponse.class);

        // then
        assertAll(
                () -> assertThat(responseProduct.getId()).isNotNull(),
                () -> assertThat(responseProduct.getName()).isEqualTo(pizza.getName()),
                () -> assertThat(responseProduct.getPrice()).isEqualTo(pizza.getPrice())
        );
    }

    @DisplayName("상품을 삭제한다.")
    @Test
    void deleteProduct() {
        // given
        var location = 상품_생성_요청(chicken).header("location");

        // when
        ExtractableResponse<Response> response = 상품_삭제_요청(location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("상품을 삭제 후 조회할 경우 INVALID_PRODUCT_ID 예외가 발생한다.")
    @Test
    void deleteProduct_AfterDelete() {
        // given
        var location = 상품_생성_요청(chicken).header("location");

        // when
        상품_삭제_요청(location);
        ExtractableResponse<Response> response = 단일_상품_조회(location);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(INVALID_PRODUCT_ID)
        );
    }

    private ExtractableResponse<Response> 상품_생성_요청(final ProductRequest product) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 단일_상품_조회(final String location) {
        return given().log().all()
                .get(location)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 상품_삭제_요청(final String location) {
        return given().log().all()
                .when()
                .delete(location)
                .then().log().all()
                .extract();
    }

    private String parseUri(String uri) {
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }
}
