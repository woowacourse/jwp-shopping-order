package cart.domain.order;

import cart.domain.bill.Bill;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.value.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("총 할인된 구매 금액이 50,000원을 미만이면 배송료는 3000이다.")
    void determine_shipping_fee() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);

        // when
        Bill result = order.makeBill();


        // then
        assertThat(result.getShippingFee()).isEqualTo(3000);
    }

    @Test
    @DisplayName("총 할인된 구매 금액이 50,000원을 이상이면 배송료는 무료이다.")
    void determine_shipping_fee_free() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 10000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 20000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);

        // when
        Bill result = order.makeBill();


        // then
        assertThat(result.getShippingFee()).isEqualTo(0);
    }
}
