package cart.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.request.ProductAddRequest;
import cart.dto.response.ProductResponse;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.List;

public class ProductIntegrationTest extends IntegrationTest {

    private static final ProductAddRequest dummyRequest = new ProductAddRequest("name", 1_000, "imageUrl", 10);
    private static final String URI = "/products";

    private ExtractableResponse<Response> postDummyProduct() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dummyRequest)
                .when()
                .post(URI)
                .then()
                .extract();
    }

    @Test
    public void 상품을_추가한다() {
        // when
        var response = postDummyProduct();
        String location = response.header("Location");
        String id = location.substring(location.length() - 1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo(URI + "/" + id);
    }

    @Test
    public void 상품을_조회한다() {
        // post product
        var postResponseBody = postDummyProduct();
        String location = postResponseBody.header("Location");

        // get product
        var response = given().log().all()
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        // then
        assertThat(response.getProductId()).isNotNull();
        assertThat(response.getName()).isEqualTo(dummyRequest.getName());
        assertThat(response.getPrice()).isEqualTo(dummyRequest.getPrice());
    }

    @Test
    public void 모든_상품을_조회한다() {
        // given
        postDummyProduct();

        // when
        var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(URI)
                .then()
                .extract();

        List<ProductResponse> body = result.jsonPath().getList(".", ProductResponse.class);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body).isNotNull();
        assertThat(body).hasSize(1);
    }

    @Test
    void 상품을_수정한다() {
        // given
        var responseBody = postDummyProduct();
        String location = responseBody.header("Location");
        String id = location.substring(location.length() - 1);

        // when
        ProductAddRequest newRequest = new ProductAddRequest("newName", 9_999, "newImageUrl", 99);
        var result = given()
                .contentType(ContentType.JSON)
                .body(newRequest)
                .when()
                .put(URI + "/{productId}", id)
                .then()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상품을_삭제한다() {
        // given
        var responseBody = postDummyProduct();
        String location = responseBody.header("Location");
        String id = location.substring(location.length() - 1);

        // when
        var result = when()
                .delete(URI + "/{productId}", id)
                .then()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 존재하지_않는_상품의_ID로_조회를_요청하면_Bad_Request를_응답한다() {
        var result = when()
                .get(URI + "/{productId}", 24124)
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
