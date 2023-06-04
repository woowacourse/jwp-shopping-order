package cart.acceptance.steps;

import static cart.acceptance.steps.CommonSteps.추가;
import static cart.acceptance.steps.Request.Builder.사용자의_요청_생성;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.Product;
import cart.domain.member.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemResponse;
import cart.dto.cart.CartItemSaveRequest;
import cart.dto.cart.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemSteps {

    public static final String 장바구니_URL = "/cart-items";

    public static String 단일_장바구니_URL(final String 번호) {
        return 장바구니_URL + "/" + 번호;
    }

    public static CartItemSaveRequest 장바구니_추가_요청_정보(final String 상품_번호) {
        return new CartItemSaveRequest(Long.valueOf(상품_번호));
    }

    public static CartItemQuantityUpdateRequest 장바구니_수량_변경_요청_정보(final Integer 수량) {
        return new CartItemQuantityUpdateRequest(수량);
    }

    public static String 장바구니_상품_번호를_구한다(final ExtractableResponse<Response> 요청_결과) {
        final int index = 2;
        return 요청_결과.header("Location").split("/")[index];
    }

    public static void 장바구니_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final CartItemResponse... 장바구니_정보
    ) {
        final List<CartItemResponse> 전체_장바구니_정보 = Arrays.stream(장바구니_정보).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getList(".", CartItemResponse.class))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(전체_장바구니_정보);
    }

    public static CartItemResponse 장바구니_정보(final Product 상품, final Integer 수량) {
        final ProductResponse productResponse = new ProductResponse(
                null,
                상품.getName(),
                상품.getImageUrl(),
                상품.getPrice().getLongValue()
        );
        return new CartItemResponse(null, 수량, productResponse);
    }

    public static String 장바구니에_상품을_추가하고_번호를_반환한다(final Member 사용자, final String 상품_번호) {
        final var 저장_요청 = 사용자의_요청_생성(사용자)
                .전송_정보(장바구니_추가_요청_정보(상품_번호))
                .요청_위치(추가, 장바구니_URL)
                .요청_결과_반환();
        return 장바구니_상품_번호를_구한다(저장_요청);
    }
}
