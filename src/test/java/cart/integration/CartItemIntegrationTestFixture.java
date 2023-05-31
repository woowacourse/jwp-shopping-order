package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemIntegrationTestFixture {

    public static ExtractableResponse<Response> 장바구니에_상품_등록_요청(Member 사용자, Long 상품_ID) {
        CartItemRequest request = new CartItemRequest(상품_ID);
        return RestAssured.given().log().all()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/cart-items")
                .then().log().all()
                .extract();
    }

    public static CartItemResponse 장바구니_응답(Long 장바구니_ID, Product 상품, int 수량) {
        return new CartItemResponse(장바구니_ID, 수량, ProductResponse.of(상품));
    }

    public static void 장바구니_전체_조회_응답_검증(ExtractableResponse<Response> 응답, CartItemResponse... cartItemResponses) {
        List<CartItemResponse> list = Arrays.asList(cartItemResponses);
        assertThat(응답.jsonPath().getList(".", CartItemResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(list);
    }

    public static ExtractableResponse<Response> 장바구니_상품_전체_조회_요청(Member 사용자) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/cart-items")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_삭제_요청(Member 사용자, Long 장바구니_ID) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("/cart-items/{id}", 장바구니_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_상품_수량_수정_요청(Member 사용자, Long 장바구니_ID, int 변경할_수량) {
        CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(변경할_수량);
        return RestAssured.given().log().all()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .patch("/cart-items/{id}", 장바구니_ID)
                .then().log().all()
                .extract();
    }
}
