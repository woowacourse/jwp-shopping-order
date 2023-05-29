package cart.acceptence.steps;

import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class ProductSteps {

    public static ExtractableResponse<Response> 전체_상품_조회_요청() {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/products")

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 단일_상품_조회_요청(final long productId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/products/" + productId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_추가_요청(final ProductRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/products")

                .then()
                .extract();
    }

    public static long 상품_추가하고_아이디_반환(final ProductRequest request) {
        final ExtractableResponse<Response> response = 상품_추가_요청(request);
        return 응답에서_상품_아이디_추출(response);
    }

    public static long 응답에서_상품_아이디_추출(final ExtractableResponse<Response> 상품_추가_응답) {
        final String location = 상품_추가_응답.header("location");
        return Long.parseLong(location.split("/")[2]);
    }

    public static ExtractableResponse<Response> 상품_수정_요청(final long productId, final ProductRequest request) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .put("/products/" + productId)

                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_삭제_요청(final long productId) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .delete("/products/" + productId)

                .then()
                .extract();
    }
}
