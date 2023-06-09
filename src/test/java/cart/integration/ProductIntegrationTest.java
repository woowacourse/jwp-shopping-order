package cart.integration;

import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    void 전체_상품을_조회하면_상태코드가_OK_이다() {
        // given, when
        ExtractableResponse<Response> response = 전체_상품_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상품을_추가하면_상태코드가_CREATED_이다() {
        // given
        ProductRequest product = new ProductRequest("치킨", 10_000L, "http://example.com/chicken.jpg", 10L);

        // when
        ExtractableResponse<Response> response = 상품_추가(product);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 특정_상품을_조회한다() {
        // given
        ProductRequest product = new ProductRequest("피자", 15_000L, "http://example.com/pizza.jpg", 10L);
        String location = 상품_추가(product).header("Location");

        // when
        ExtractableResponse<Response> response = 특정_상품_조회(location);
        ProductResponse productResponse = response.as(ProductResponse.class);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(productResponse.getId()).isNotNull();
        softAssertions.assertThat(productResponse.getName()).isEqualTo(product.getName());
        softAssertions.assertThat(productResponse.getPrice()).isEqualTo(product.getPrice());
        softAssertions.assertAll();
    }

    @Test
    void 존재하지_않는_상품을_조회하면_상태코드가_BAD_REQUEST_이다() {
        // given
        ProductRequest product = new ProductRequest("피자", 15_000L, "http://example.com/pizza.jpg", 10L);
        String location = 상품_추가(product).header("Location");

        // when
        ExtractableResponse<Response> response = 특정_상품_조회(location + "11");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        ProductRequest firstProduct = new ProductRequest("피자", 15_000L, "http://example.com/pizza.jpg", 10L);
        ProductRequest secondProduct = new ProductRequest("치킨", 20_000L, "http://example.com/chicken.jpg", 30L);
        상품_추가(firstProduct);
        상품_추가(secondProduct);

        // when
        ExtractableResponse<Response> response = 전체_상품_조회();
        List<ProductResponse> productResponses = response.jsonPath().getList(".", ProductResponse.class);
        List<String> productNames = productResponses.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());
        List<Long> productPrices = productResponses.stream()
                .map(ProductResponse::getPrice)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(productResponses.size()).isEqualTo(2);
        softAssertions.assertThat(productNames).containsAll(List.of(firstProduct.getName(), secondProduct.getName()));
        softAssertions.assertThat(productPrices).containsAll(List.of(firstProduct.getPrice(), secondProduct.getPrice()));
        softAssertions.assertAll();
    }

    @Test
    void 상품을_업데이트한다() {
        // given
        ProductRequest product = new ProductRequest("피자", 15_000L, "http://example.com/pizza.jpg", 10L);
        String location = 상품_추가(product).header("Location");
        Long productId = Long.parseLong(location.split("/")[2]);
        ProductRequest updateProduct = new ProductRequest("치킨", 20_000L, "http://example.com/chicken.jpg", 20L);

        // when
        ExtractableResponse<Response> response = 상품_업데이트(productId, updateProduct);

        // then
        ProductResponse updateProductResponse = 특정_상품_조회(location).as(ProductResponse.class);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(updateProductResponse.getName()).isEqualTo(updateProduct.getName());
        softAssertions.assertThat(updateProductResponse.getPrice()).isEqualTo(updateProduct.getPrice());
        softAssertions.assertThat(updateProductResponse.getImageUrl()).isEqualTo(updateProduct.getImageUrl());
        softAssertions.assertThat(updateProductResponse.getStock()).isEqualTo(updateProduct.getStock());
        softAssertions.assertAll();
    }

    @Test
    void 상품을_삭제한다() {
        // given
        ProductRequest product = new ProductRequest("피자", 15_000L, "http://example.com/pizza.jpg", 10L);
        String location = 상품_추가(product).header("Location");
        Long productId = Long.parseLong(location.split("/")[2]);

        // when
        ExtractableResponse<Response> response = 상품_삭제(productId);

        // then
        ExtractableResponse<Response> findResponse = 특정_상품_조회(location);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        softAssertions.assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        softAssertions.assertAll();
    }

    private ExtractableResponse<Response> 전체_상품_조회() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 특정_상품_조회(String location) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(location)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 상품_추가(ProductRequest product) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 상품_업데이트(Long productId, ProductRequest product) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .put("/products/" + productId)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 상품_삭제(Long productId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/products/" + productId)
                .then()
                .extract();
    }
}
