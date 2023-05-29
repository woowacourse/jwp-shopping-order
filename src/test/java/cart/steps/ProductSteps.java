package cart.steps;

import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;

public class ProductSteps {

    public static ExtractableResponse<Response> 제품_추가_요청(final ProductRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .post("/products")

                .then()
                .log().all()
                .extract();
    }

    public static Long 제품_추가하고_아이디_반환(final ProductRequest request) {
        return 추가된_제품_아이디_반환(제품_추가_요청(request));
    }

    public static Long 추가된_제품_아이디_반환(final ExtractableResponse<Response> response) {
        String locationHeader = response.header("Location");
        return Long.parseLong(locationHeader.replace("/products/", ""));
    }

    public static ExtractableResponse<Response> 제품_정보_업데이트_요청(final Long id, final ProductRequest request) {
        return RestAssured.given()
                .log().all()
                .contentType(JSON)
                .body(request)

                .when()
                .put("/products/" + id)

                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 제품_삭제_요청(final Long id) {
        return RestAssured.given()
                .log().all()

                .when()
                .delete("/products/" + id)

                .then()
                .log().all()
                .extract();
    }
}
