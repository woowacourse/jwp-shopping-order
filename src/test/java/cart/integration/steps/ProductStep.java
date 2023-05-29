package cart.integration.steps;

import static cart.integration.steps.CommonStep.헤더_ID_값_파싱;
import static io.restassured.RestAssured.given;

import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductStep {
    public static Long 상품_생성_요청후_상품_ID를_리턴한다(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return 헤더_ID_값_파싱(response);
    }

    public static ProductRequest 상품_생성_요청_생성(String 상품_이름, int 상품_가격, String 상품_이미지_URL) {
        return new ProductRequest(상품_이름, 상품_가격, 상품_이미지_URL);
    }
}
