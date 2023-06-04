package cart.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.Product;
import cart.dto.order.OrderItemResponse;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSaveRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderSteps {

    public static final String 주문_URL = "/orders";

    public static String 단일_주문_URL(final String 번호) {
        return 주문_URL + "/" + 번호;
    }

    public static OrderSaveRequest 주문_요청_정보(final List<String> 주문_상품_번호, final String 쿠폰_번호) {
        final List<Long> orderIds = 주문_상품_번호.stream().map(Long::valueOf).collect(Collectors.toList());
        final Long couponId = Objects.nonNull(쿠폰_번호) ? Long.valueOf(쿠폰_번호) : null;
        return new OrderSaveRequest(orderIds, couponId);
    }

    public static String 주문_번호를_구한다(final ExtractableResponse<Response> 요청_결과) {
        final int index = 2;
        return 요청_결과.header("Location").split("/")[index];
    }

    public static OrderResponse 주문_정보(
            final Long 총_상품_가격,
            final Long 할인_가격,
            final Long 배달비,
            final OrderItemResponse... 주문_상품
    ) {
        final List<OrderItemResponse> 전체_주문_상품 = Arrays.stream(주문_상품).collect(Collectors.toList());
        return new OrderResponse(null, 총_상품_가격, 할인_가격, 배달비, 전체_주문_상품);
    }

    public static OrderItemResponse 주문_상품_정보(
            final Product 상품,
            final Integer 개수
    ) {
        return new OrderItemResponse(null, 상품.getName(), 상품.getPrice().getLongValue(), 상품.getImageUrl(), 개수);
    }

    public static void 전체_주문_정보를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final OrderResponse... 주문_정보
    ) {
        final List<OrderResponse> 전체_주문_정보 = Arrays.stream(주문_정보).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getList(".", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(전체_주문_정보);
    }

    public static void 주문_정보를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final OrderResponse 주문_정보
    ) {
        assertThat(요청_결과.jsonPath().getObject(".", OrderResponse.class))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(주문_정보);
    }
}
