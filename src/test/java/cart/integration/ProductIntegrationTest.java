package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.integration.step.ProductStep;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class ProductIntegrationTest extends IntegrationTest {
    @DisplayName("상품 목록을 조회한다")
    @Test
    public void getProducts() {
        // when
        ExtractableResponse<Response> 상품_목록_조회_응답 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        // then
        assertThat(상품_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("상품을 추가한다")
    @Test
    public void createProduct() {
        // given
        ProductRequest 치킨 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        // when
        ExtractableResponse<Response> 상품_추가_응답 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(치킨)
                .when()
                .post("/products")
                .then()
                .extract();

        // then
        assertThat(상품_추가_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("추가된 상품을 조회한다.")
    @Test
    public void getCreatedProduct() {
        // given
        ProductRequest 피자 = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");
        String 추가된_피자_조회_주소 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(피자)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        // when
        ProductResponse 추기된_피자 = given().log().all()
                .when()
                .get(추가된_피자_조회_주소)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        // then
        Assertions.assertAll(
                () -> assertThat(추기된_피자.getId()).isNotNull(),
                () -> assertThat(추기된_피자.getName()).isEqualTo("피자"),
                () -> assertThat(추기된_피자.getPrice()).isEqualTo(15_000)
        );
    }

    @DisplayName("상품을 변경한다")
    @Test
    public void updateProduct() {
        // given
        ProductRequest 치킨 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        ProductRequest 피자 = new ProductRequest("피자", 10_000, "http://example.com/chicken.jpg");
        Long 치킨_ID = ProductStep.상품_추가_응답에서_아이디를_가져온다(치킨);

        // when
        ExtractableResponse<Response> 상품_변경_응답 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(피자)
                .when()
                .put("/products/" + 치킨_ID)
                .then()
                .extract();
        ExtractableResponse<Response> 상품_조회_응답 = ProductStep.상품을_상세_조회한다(치킨_ID);

        // then
        Assertions.assertAll(
                () -> assertThat(상품_변경_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(상품_조회_응답.as(ProductResponse.class).getName()).isEqualTo("피자")
        );
    }

    @DisplayName("상품을 삭제한다")
    @Test
    public void deleteProduct() {
        // given
        ProductRequest 치킨 = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        Long 치킨_ID = ProductStep.상품_추가_응답에서_아이디를_가져온다(치킨);

        // when
        ExtractableResponse<Response> 상품_삭제_응답 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(치킨)
                .when()
                .delete("/products/" + 치킨_ID)
                .then()
                .extract();

        // then
        assertThat(상품_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
