package cart.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.Product;
import cart.dto.cart.ProductDto;
import cart.dto.cart.ProductSaveRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductSteps {

    public static ExtractableResponse<Response> 상품_추가_요청(final String 상품명, final String 이미지, final Long 가격) {
        final ProductSaveRequest request = new ProductSaveRequest(상품명, 이미지, 가격);

        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_추가_요청(final Product 상품) {
        final ProductSaveRequest request = new ProductSaveRequest(
                상품.getName(),
                상품.getImageUrl(),
                상품.getPrice().getLongValue()
        );

        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    public static String 상품_번호를_구한다(final ExtractableResponse<Response> 요청_결과) {
        final int index = 2;
        return 요청_결과.header("Location").split("/")[index];
    }

    public static ExtractableResponse<Response> 상품_조회_요청(final String 상품_번호) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/products/" + 상품_번호)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_전체_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final String 상품_번호,
            final Product 상품
    ) {
        assertThat(요청_결과.jsonPath().getObject(".", ProductDto.class))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new ProductDto(
                        Long.parseLong(상품_번호),
                        상품.getName(),
                        상품.getImageUrl(),
                        상품.getPrice().getLongValue()
                ));
    }

    public static ProductDto 상품_정보(final String 상품_번호, final Product 상품) {
        return new ProductDto(Long.parseLong(상품_번호), 상품.getName(), 상품.getImageUrl(), 상품.getPrice().getLongValue());
    }

    public static void 상품_전체_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final ProductDto... 상품_정보
    ) {
        final List<ProductDto> 전체_상품_정보 = Arrays.stream(상품_정보).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getList(".", ProductDto.class))
                .usingRecursiveComparison()
                .isEqualTo(전체_상품_정보);
    }
}
