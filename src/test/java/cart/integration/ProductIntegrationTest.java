package cart.integration;

import static cart.fixture.JsonMapper.toJson;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductController 통합 테스트은(는)")
public class ProductIntegrationTest extends IntegrationTest {

    private static final String API_URL = "/products";


    @Test
    void 상품을_추가한다() {
        // when
        var response = 상품_추가("치킨", 10_000, "http://example.com/chicken.jpg");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotEmpty();
    }

    @Test
    void 모든_상품을_조회한다() {
        //given
        상품_추가("치킨", 10_000, "http://example.com/chicken.jpg");
        상품_추가("피자", 20_000, "http://example.com/pizza.jpg");

        // when
        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .extract();

        // then
        List<ProductResponse> result = response.as(
                new ParameterizedTypeReference<List<ProductResponse>>() {
                }.getType()
        );

        ProductResponse 첫번째상품 = result.get(0);
        ProductResponse 두번째상품 = result.get(1);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        상품_검증(첫번째상품, "치킨", 10_000, "http://example.com/chicken.jpg");
        상품_검증(두번째상품, "피자", 20_000, "http://example.com/pizza.jpg");
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        String 위치 = 상품을_추가하고_위치를_반환("치킨", 10_000, "http://example.com/chicken.jpg");

        // when
        var response = 상품_조회(위치);

        // then
        상품_검증(response, "치킨", 10_000, "http://example.com/chicken.jpg");
    }

    @Test
    void 상품을_수정한다() {
        // given
        String 위치 = 상품을_추가하고_위치를_반환("치킨", 10_000, "http://example.com/chicken.jpg");
        ProductRequest 비싸진치킨 = new ProductRequest("비싸진치킨", 15_000, "http://example.com/chicken.jpg");

        // when
        given().log().all()
                .contentType(JSON)
                .body(toJson(비싸진치킨))
                .when()
                .put(위치)
                .then()
                .log().all();

        // then
        var response = 상품_조회(위치);
        상품_검증(response, "비싸진치킨", 15_000, "http://example.com/chicken.jpg");
    }

    @Test
    void 상품을_삭제한다() {
        // given
        String 위치 = 상품을_추가하고_위치를_반환("치킨", 10_000, "www.naver.com");

        // when
        given().log().all()
                .contentType(JSON)
                .when()
                .delete(위치)
                .then()
                .log().all();

        // then
        var response = 상품_조회(위치);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static ExtractableResponse<Response> 상품_추가(String 이름, int 가격, String 이미지_url) {
        var request = new ProductRequest(이름, 가격, 이미지_url);
        return 상품_추가(request);
    }

    public static long 상품을_추가하고_아이디를_반환(String 이름, int 가격, String 이미지_url) {
        String location = 상품을_추가하고_위치를_반환(이름, 가격, 이미지_url);
        final String id = location.substring(location.lastIndexOf("/") + 1);
        return Long.parseLong(id);
    }

    public static String 상품을_추가하고_위치를_반환(String 이름, int 가격, String 이미지_url) {
        var request = new ProductRequest(이름, 가격, 이미지_url);
        return 상품_추가(request).header("Location");
    }

    public static ExtractableResponse<Response> 상품_추가(ProductRequest request) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .extract();
    }

    private void 상품_검증(ExtractableResponse<Response> 응답, String 이름, int 가격, String 이미지_URL) {
        var 상품 = 응답.as(ProductResponse.class);
        상품_검증(상품, 이름, 가격, 이미지_URL);
    }

    private void 상품_검증(ProductResponse 상품, String 이름, int 가격, String 이미지_URL) {
        assertAll(
                () -> assertThat(상품.getId()).isPositive(),
                () -> assertThat(상품.getName()).isEqualTo(이름),
                () -> assertThat(상품.getPrice()).isEqualTo(가격),
                () -> assertThat(상품.getImageUrl()).isEqualTo(이미지_URL)
        );
    }

    private ExtractableResponse<Response> 상품_조회(String 위치) {
        return given().log().all()
                .when()
                .get(위치)
                .then()
                .extract();
    }
}
