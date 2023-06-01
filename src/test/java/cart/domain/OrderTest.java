package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTest {

    @ParameterizedTest(name = "{displayName}")
    @CsvSource(value = {"50_000, 0", "49_999, 3_000"})
    @DisplayName("총 할인된 구매 금액이 50,000원을 이상이면 배송료는 무료이고 그렇지 않으면 배송료는 3,000원 입니다.")
    void determine_shipping_fee(int purchasePrice, int expect) {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);

        // when
        order.determineShippingFee(purchasePrice);
        int result = order.getShippingFee();

        // then
        assertThat(result).isEqualTo(expect);
    }

    @ParameterizedTest(name = "{displayName}")
    @MethodSource("provideWrongRequest")
    @DisplayName("계산되 주문서와 요청된 주문서가 다르면 에러를 발생한다.")
    void check_request_bill(List<Integer> request) {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0), // 8000원
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10)); // 27000원
        Order order = new Order(member, orderItems);
        order.calculatePrice();

        // when + then
        assertThatThrownBy(() -> order.validateBill(request.get(0), request.get(1), request.get(2),request.get(3),request.get(4)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideWrongRequest() {
        return Stream.of(
                // 전체 금액이 틀린 것
                Arguments.of(List.of(38_000, 35_000, 3_000, 2_000, 3_000)),
                // 할인 금액이 틀린 것
                Arguments.of(List.of(40_000, 30_000, 3_000, 2_000, 3_000)),
                // 상품 할인 금액이 틀린 것
                Arguments.of(List.of(40_000, 33_000, 2_000, 2_000, 3_000)),
                // 멤버 할인 금액이 틀린 것
                Arguments.of(List.of(40_000, 30_000, 3_000, 3_000, 3_000)),
                // 배송비가 틀린 것
                Arguments.of(List.of(40_000, 30_000, 3_000, 2_000, 0))
        );
    }
}
