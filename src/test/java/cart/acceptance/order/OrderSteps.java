package cart.acceptance.order;

import static cart.acceptance.common.CommonAcceptanceSteps.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.member.domain.Member;
import cart.order.presentation.dto.OrderResponse;
import cart.order.presentation.dto.OrderResponse.OrderItemResponse;
import cart.order.presentation.dto.OrderResponses;
import cart.order.presentation.dto.PlaceOrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;

@SuppressWarnings("NonAsciiCharacters")
public class OrderSteps {

    public static ExtractableResponse<Response> 상품_주문_요청(
            Member 회원,
            Long... 장바구니_상품_ID들
    ) {
        return given(회원)
                .body(new PlaceOrderRequest(Arrays.asList(장바구니_상품_ID들)))
                .when().post("/orders")
                .then().log().all()
                .extract();
    }

    public static OrderResponse 단일_주문_정보_조회_요청(
            Member 회원,
            Long 주문_ID
    ) {
        return given(회원)
                .when().get("/orders/{id}", 주문_ID)
                .then()
                .extract()
                .as(OrderResponse.class);
    }

    public static OrderItemResponse 주문_상품_정보(
            Long 주문상품_ID,
            String 이름,
            int 가격,
            int 수량,
            String 이미지_주소
    ) {
        return new OrderItemResponse(주문상품_ID, 이름, 가격, 수량, 이미지_주소);
    }

    public static OrderResponse 주문_정보(
            Long 주문_ID,
            int 총액,
            OrderItemResponse... 주문_상품_정보들
    ) {
        return new OrderResponse(주문_ID, Arrays.asList(주문_상품_정보들), 총액);
    }

    public static OrderResponses 주문들_정보(
            OrderResponse... 주문들
    ) {
        return new OrderResponses(Arrays.asList(주문들));
    }

    public static void 주문_정보_검증(
            OrderResponse 실제,
            OrderResponse 예상
    ) {
        assertThat(실제).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(예상);
    }

    public static OrderResponses 회원의_주문_정보_조회_요청(
            Member 회원
    ) {
        return given(회원)
                .when().get("/orders")
                .then()
                .extract()
                .as(OrderResponses.class);
    }

    public static void 주문들_정보_검증(
            OrderResponses 실제,
            OrderResponses 예상
    ) {
        assertThat(실제).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(예상);
    }
}
