package cart.acceptance;

import static cart.acceptance.CommonSteps.LOCATION_헤더에서_ID_추출;
import static io.restassured.RestAssured.given;

import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class ProductSteps {

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest createRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when()
                .post("/products")
                .then()
                .extract();
    }

    public static Long 상품_생성하고_아이디_반환(ProductRequest createRequest) {
        ExtractableResponse<Response> response = 상품_생성_요청(createRequest);
        return LOCATION_헤더에서_ID_추출(response);
    }

    public static ExtractableResponse<Response> 상품_수정_요청(long productId, ProductRequest updateRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateRequest)
                .when()
                .put("/products/{id}", productId)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_삭제_요청(long productId) {
        return given()
                .when()
                .delete("/products/{id}", productId)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 특정_상품_조회_요청(long productId) {
        return given()
                .when()
                .get("/products/{id}", productId)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 모든_상품_조회_요청() {
        return given()
                .when()
                .get("/products")
                .then()
                .extract();
    }
}
