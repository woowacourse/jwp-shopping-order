package cart.domain.bill;

import cart.domain.value.Money;
import cart.exception.bill.InvalidBillRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BillTest {

    @ParameterizedTest(name = "{displayName}")
    @MethodSource("provideWrongRequest")
    @DisplayName("계산되 주문서와 요청된 주문서가 다르면 에러를 발생한다.")
    void check_request_bill(List<Integer> request) {
        // given
        Bill bill = new Bill(
                new Money(3_000),
                new Money(2_000),
                new Money(40_000),
                new Money(35_000),
                new Money(3_000),
                new Money(38_000));

        // when + then
        assertThatThrownBy(() -> bill.validateRequest(request.get(0), request.get(1), request.get(2), request.get(3), request.get(4)))
                .isInstanceOf(InvalidBillRequestException.class);
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

    @Test
    @DisplayName("총 금액이 할인된 금액 + 배송료가 아니면 에러를 발생한다.")
    void validate_total_price() {
        // when + then
        assertThatThrownBy(() -> new Bill(
                new Money(3_000),
                new Money(2_000),
                new Money(40_000),
                new Money(35_000),
                new Money(3_000),
                new Money(37_000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("총 할인된 금액이 멤버할인금액 + 상품할인금액이 아니면 에러를 발생한다.")
    void validate_total_discount_price() {
        // when + then
        assertThatThrownBy(() -> new Bill(
                new Money(3_000),
                new Money(6_000),
                new Money(40_000),
                new Money(35_000),
                new Money(3_000),
                new Money(38_000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("총 할인된 금액이 50,000원 이상일 때 배송비가 3,000원이면 에러를 발생한다.")
    void validate_shipping_fee_free() {
        // when + then
        assertThatThrownBy(() -> new Bill(
                new Money(3_000),
                new Money(2_000),
                new Money(40_000),
                new Money(35_000),
                new Money(0),
                new Money(38_000)))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("총 할인된 금액이 50,000원 미만일 때 배송비가 0원이면 에러를 발생한다.")
    void validate_shipping_fee_standard() {
        // when + then
        assertThatThrownBy(() -> new Bill(
                new Money(3_000),
                new Money(2_000),
                new Money(55_000),
                new Money(50_000),
                new Money(3_000),
                new Money(53_000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
