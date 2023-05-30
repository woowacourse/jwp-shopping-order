package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTest {

    @Test
    @DisplayName("주문 시 최종 구매 금액(할인 금액)을 계산한다.")
    void calculate_discounted_purchase_money() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);

        int expect = 35_000;

        // when
        int result = order.calculateTotalDiscountedPrice();

        // then
        assertThat(result).isEqualTo(expect);
    }

    @Test
    @DisplayName("원래의 총 상품 구매 금액을 구한다.")
    void calculate_principle_purchase_money() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);

        int expect = 40_000;

        // when
        order.calculateTotalPrinciplePrice();
        int result = order.getPurchaseItemPrice();

        // then
        assertThat(result).isEqualTo(expect);
    }
}
