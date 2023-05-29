package cart.acceptance.cartitem;

import static cart.acceptance.common.CommonAcceptanceSteps.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.cartitem.presentation.dto.AddCartItemRequest;
import cart.cartitem.presentation.dto.CartItemResponse;
import cart.cartitem.presentation.dto.UpdateCartItemQuantityRequest;
import cart.member.domain.Member;
import cart.product.presentation.dto.ProductResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemSteps {

    public static ExtractableResponse<Response> 장바구니_상품_추가_요청(
            Member 회원,
            Long 상품_ID
    ) {
        return given(회원)
                .body(new AddCartItemRequest(상품_ID))
                .when()
                .post("/cart-items")
                .then().log().all()
                .extract();
    }

    public static List<CartItemResponse> 장바구니_상품_전체_조회_요청(
            Member 회원
    ) {
        return given(회원)
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract()
                .as(new TypeRef<>() {
                });
    }

    public static CartItemResponse 예상_장바구니_상품_정보(
            Long ID,
            int 수량,
            ProductResponse 예상_상품_정보
    ) {
        return new CartItemResponse(ID, 수량, 예상_상품_정보);
    }

    public static void 장바구니_상품_정보_검증(
            List<CartItemResponse> 실제,
            List<CartItemResponse> 예상
    ) {
        assertThat(실제).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(예상);
    }

    public static ExtractableResponse<Response> 장바구니_상품_수량_변경_요청(
            Member 회원,
            Long 장바구니_상품_ID,
            int 변경할_수량
    ) {
        return given(회원)
                .when()
                .body(new UpdateCartItemQuantityRequest(변경할_수량))
                .patch("/cart-items/{cartItemId}", 장바구니_상품_ID)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니_상품_제거_요청(
            Member 회원,
            Long 장바구니_상품_ID
    ) {
        return given(회원)
                .when()
                .delete("/cart-items/{cartItemId}", 장바구니_상품_ID)
                .then()
                .log().all()
                .extract();
    }
}
