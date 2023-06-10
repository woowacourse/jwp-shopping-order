package cart.acceptance.product;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.product.presentation.dto.ProductResponse;
import cart.product.presentation.dto.UpdateProductRequest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class ProductSteps {

    public static ExtractableResponse<Response> 상품_생성_요청(
            String 이름,
            int 가격,
            String 이미지_주소
    ) {
        return given()
                .contentType(ContentType.JSON)
                .body(new UpdateProductRequest(이름, 가격, 이미지_주소))
                .when()
                .post("/products")
                .then()
                .extract();
    }

    public static ProductResponse 단일_상품_조회_요청(
            Long 상품_ID
    ) {
        return given().log().all()
                .when()
                .get("/products/{id}", 상품_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProductResponse.class);
    }

    public static ProductResponse 예상_상품_정보(
            Long ID,
            String 이름,
            int 가격,
            String 이미지_주소
    ) {
        return new ProductResponse(ID, 이름, 가격, 이미지_주소);
    }

    public static void 상품_조회_결과를_검증한다(
            ProductResponse 상품_정보,
            ProductResponse 예상_결과
    ) {
        assertThat(상품_정보).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(예상_결과);
    }

    public static List<ProductResponse> 모든_상품_조회_요청() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/products")
                .then().log().all()
                .extract()
                .as(new TypeRef<>() {
                });
    }

    public static void 상품_전체_조회_결과를_검증한다(
            List<ProductResponse> 상품_정보들,
            List<ProductResponse> 예상_결과
    ) {
        assertThat(상품_정보들).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(예상_결과);
    }
}
