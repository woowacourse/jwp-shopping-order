package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.value.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderItemTest {

    @Test
    @DisplayName("상품 할인을 통한 상품의 할인된 가격을 구한다.")
    void calculate_discount_price_by_item_discount() {
        // given
        OrderItem orderItem = new OrderItem(1L, "포카칩", 1000, "이미지", 5, 10);
        Money expect = new Money(4500);

        // when
        Money result = orderItem.getItemDiscountedPrice();

        // then
        assertThat(result).isEqualTo(expect);
    }

    @Test
    @DisplayName("멤버 할인을 통한 상품의 할인된 가격을 구한다.")
    void calculate_discount_price_by_member_discount() {
        // given
        OrderItem orderItem = new OrderItem(1L, "포카칩", 1000, "이미지", 5, 10);
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
        Money expect = new Money(4000);

        // when
        Money result = orderItem.getMemberDiscountedPrice(member);

        // then
        assertThat(result).isEqualTo(expect);
    }
}
