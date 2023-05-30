package cart.integration;

import static cart.integration.steps.CommonStep.헤더_ID_값_파싱;
import static cart.integration.steps.ProductStep.상품_삭제_요청;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.dto.ProductRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductIntegrationTest extends IntegrationTest {

    private final ProductEntity 상품 = new ProductEntity(null,
            "피자", 100000, "https://example.com/pizza.jpg",
            null, null);
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 상품_정보를_입력_받아_상품을_등록한다() {
        ProductRequest 요청 = 상품_등록_요청_생성(상품.getName(), 상품.getPrice(), 상품.getName());

        var 응답 = 상품_등록_요청(요청);
        long 상품_ID = 헤더_ID_값_파싱(응답);

        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(응답.header(HttpHeaders.LOCATION)).isEqualTo("/products/" + 상품_ID)
        );
    }

    private ExtractableResponse<Response> 상품_등록_요청(final ProductRequest 요청) {
        return given()
                .contentType(JSON)
                .body(toJson(요청))
                .when().post("/products")
                .then()
                .log().all()
                .extract();
    }

    private ProductRequest 상품_등록_요청_생성(final String 이름, final int 가격, final String 이미지_URL) {
        return new ProductRequest(이름, 가격, 이미지_URL);
    }

    @Test
    public void 수정된_정보를_입력_받아_상품을_수정한다() {
        Long 상품_ID = 상품을_등록한다(상품);

        ProductRequest 수정_요청 = 상품_수정_요청_생성("치킨", 상품.getPrice(), 상품.getImageUrl());
        ExtractableResponse<Response> response = 상품_수정_요청(상품_ID, 수정_요청);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 상품_수정_요청(final Long 상품_ID, final ProductRequest 요청) {
        return given()
                .contentType(JSON)
                .body(toJson(요청))
                .when().put("/products/" + 상품_ID)
                .then()
                .log().all()
                .extract();
    }

    private ProductRequest 상품_수정_요청_생성(final String 이름, final int 가격, final String 이미지_URL) {
        return new ProductRequest(이름, 가격, 이미지_URL);
    }

    @Test
    public void 상품을_삭제한다() {
        final Long 상품_ID = 상품을_등록한다(상품);

        ExtractableResponse<Response> response = 상품_삭제_요청(상품_ID);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void 없는_상품_ID로_수정시_예외가_발생한다() {
        상품을_등록한다(상품);
        final Long 없는_상품_ID = Long.MAX_VALUE;
        ProductRequest 수정_요청 = 상품_수정_요청_생성("치킨", 1000, "새로운이미지");

        var 응답 = 상품_수정_요청(없는_상품_ID, 수정_요청);
        assertThat(응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void 없는_상품_ID로_삭제시_예외가_발생한다() {
        상품을_등록한다(상품);
        final Long 없는_상품_ID = Long.MAX_VALUE;

        ExtractableResponse<Response> response = 상품_삭제_요청(없는_상품_ID);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Long 상품을_등록한다(final ProductEntity 상품) {
        return productDao.save(상품);
    }

    private String toJson(final ProductRequest productModifyRequest) {
        try {
            return objectMapper.writeValueAsString(productModifyRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
