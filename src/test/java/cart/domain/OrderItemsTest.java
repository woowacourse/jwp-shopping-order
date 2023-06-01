package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemsTest {

    @Test
    @DisplayName("주문 시 최종 구매 금액(할인 금액)을 계산한다.")
    void calculate_discounted_purchase_money() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
        OrderItems orderItems = new OrderItems(List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10)));

        int expect = 35_000;

        // when
        int result = orderItems.getTotalDiscountedPrice(member);

        // then
        assertThat(result).isEqualTo(expect);
    }

    @Test
    @DisplayName("원래의 총 상품 구매 금액을 구한다.")
    void calculate_principle_purchase_money() {
        // given
        OrderItems orderItems = new OrderItems(List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10)));

        int expect = 40_000;

        // when
        int result = orderItems.getTotalPrinciplePrice();

        // then
        assertThat(result).isEqualTo(expect);
    }


    @Test
    @DisplayName("멤버 할인 금액을 구하는 기능 추가")
    void calculate_member_benefit() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
        OrderItems orderItems = new OrderItems(List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10)));

        int expect = 2000;

        // when
        int result = orderItems.getMemberBenefit(member);

        // then
        assertThat(result).isEqualTo(expect);
    }

}
