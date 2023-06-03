package cart.acceptance;

import static cart.acceptance.CommonSteps.*;
import static cart.acceptance.ProductSteps.모든_상품_조회_요청;
import static cart.acceptance.ProductSteps.상품_삭제_요청;
import static cart.acceptance.ProductSteps.상품_생성_요청;
import static cart.acceptance.ProductSteps.상품_생성하고_아이디_반환;
import static cart.acceptance.ProductSteps.상품_수정_요청;
import static cart.acceptance.ProductSteps.특정_상품_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductController 인수테스트")
@Sql(scripts = {"/delete.sql", "/schema.sql"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductAcceptanceTest {

    @Autowired
    private ProductDao productDao;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 상품을_추가한다() {
        // given
        var createRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);

        // when
        var response = 상품_생성_요청(createRequest);

        // then
        STATUS_CODE를_검증한다(response, 정상_생성);
        LOCATION_헤더를_검증한다(response);
    }

    @Test
    void 상품을_수정한다() {
        // given
        var createRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Long productId = 상품_생성하고_아이디_반환(createRequest);

        var updateRequest = new ProductRequest("새로운 떡볶이", 7000, "http://example.com/tteokbboki.jpg", 30);

        // when
        var response = 상품_수정_요청(productId, updateRequest);

        // then
        STATUS_CODE를_검증한다(response, 정상_처리);
        상품_수정_결과를_검증한다(productId);
    }

    @Test
    void 상품을_삭제한다() {
        // given
        var createRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Long productId = 상품_생성하고_아이디_반환(createRequest);

        // when
        var response = 상품_삭제_요청(productId);

        // then
        STATUS_CODE를_검증한다(response, 정상_삭제);
        상품_삭제_결과를_검증한다(productId);
    }

    @Test
    void 특정_상품을_조회한다() {
        // given
        var createRequest1 = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        var createRequest2 = new ProductRequest("치킨", 10000, "http://example.com/chicken.jpg", 50);
        상품_생성_요청(createRequest1);
        Long targetProductId = 상품_생성하고_아이디_반환(createRequest2);

        // when
        var response = 특정_상품_조회_요청(targetProductId);

        // then
        STATUS_CODE를_검증한다(response, 정상_처리);
        특정_상품_조회_결과를_검증한다(response, createRequest2);
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        var createRequest1 = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        var createRequest2 = new ProductRequest("치킨", 10000, "http://example.com/chicken.jpg", 50);
        Long productId1 = 상품_생성하고_아이디_반환(createRequest1);
        Long productId2 = 상품_생성하고_아이디_반환(createRequest2);

        // when
        var response = 모든_상품_조회_요청();

        // then
        STATUS_CODE를_검증한다(response, 정상_처리);
        모든_상품_조회_결과를_검증한다(response, List.of(productId1, productId2), List.of(createRequest1, createRequest2));
    }

    private void 상품_수정_결과를_검증한다(Long productId) {
        Product findProduct = productDao.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 product가 존재하지 않습니다.."));
        assertThat(findProduct.getName()).isEqualTo("새로운 떡볶이");
        assertThat(findProduct.getPrice()).isEqualTo(7000);
        assertThat(findProduct.getImageUrl()).isEqualTo("http://example.com/tteokbboki.jpg");
        assertThat(findProduct.getStock()).isEqualTo(30);
    }

    private void 상품_삭제_결과를_검증한다(Long productId) {
        assertThatThrownBy(() -> productDao.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 product가 존재하지 않습니다.")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 product가 존재하지 않습니다.");
    }

    private void 특정_상품_조회_결과를_검증한다(ExtractableResponse<Response> response, ProductRequest productRequest) {
        ProductResponse productResponse = response.as(ProductResponse.class);

        assertThat(productResponse.getName()).isEqualTo(productRequest.getName());
        assertThat(productResponse.getPrice()).isEqualTo(productRequest.getPrice());
        assertThat(productResponse.getImageUrl()).isEqualTo(productRequest.getImageUrl());
        assertThat(productResponse.getStock()).isEqualTo(productRequest.getStock());
    }

    private void 모든_상품_조회_결과를_검증한다(ExtractableResponse<Response> response, List<Long> productIds,
                                   List<ProductRequest> requests) {
        List<ProductResponse> expectedResponses = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            ProductResponse productResponse = makeResponse(productIds.get(i), requests.get(i));
            expectedResponses.add(productResponse);
        }

        List<ProductResponse> actualResponses = response.as(new TypeRef<>() {});
        assertThat(actualResponses).usingRecursiveComparison()
                .isEqualTo(expectedResponses);
    }

    private ProductResponse makeResponse(Long productId, ProductRequest productRequest) {
        return new ProductResponse(productId, productRequest.getPrice(), productRequest.getName(),
                productRequest.getImageUrl(), productRequest.getStock());
    }
}
