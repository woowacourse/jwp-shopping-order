package cart.domain.order;

import static cart.fixtures.CartItemFixtures.맥북_ID_5_1개_1500000원;
import static cart.fixtures.CartItemFixtures.바닐라_크림_콜드브루_ID_4_3개_17400원;
import static cart.fixtures.CartItemFixtures.아메리카노_ID_3_8개_36000원;
import static cart.fixtures.CartItemFixtures.유자_민트_티_ID_1_5개_29500원;
import static cart.fixtures.CartItemFixtures.자몽_허니_블랙티_ID_2_7개_39900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 객체의 Member와 장바구니 상품들의 Member가 다르면 예외가 발생한다")
    @Test
    void createOrder_differentMemberWithCartItems_throws() {
        final Member member = new Member(1L, "test@email.com", "password");
        final Member otherMember = new Member(2L, "other@email.com", "password");
        final List<CartItem> items = List.of(유자_민트_티_ID_1_5개_29500원(member), 바닐라_크림_콜드브루_ID_4_3개_17400원(member));
        final CartItems cartItems = new CartItems(items);
        final FixedDiscountPolicy discountPolicy = FixedDiscountPolicy.from(cartItems.sumOfPrice());

        // when, then
        assertThatThrownBy(() -> new Order(otherMember, cartItems, discountPolicy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니 상품을 추가한 Member와 주문을 생성한 Member가 일치하지 않습니다");
    }

    @DisplayName("총 금액이 5만원 이하인 주문에는 할인이 적용되지 않는다")
    @Test
    void createOrder_priceUnder50000_noDiscount() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final List<CartItem> items = List.of(유자_민트_티_ID_1_5개_29500원(member), 바닐라_크림_콜드브루_ID_4_3개_17400원(member));
        final CartItems cartItems = new CartItems(items);
        final FixedDiscountPolicy discountPolicy = FixedDiscountPolicy.from(cartItems.sumOfPrice());

        // when
        final Order order = new Order(member, cartItems, discountPolicy);

        // then
        assertThat(order.getOriginalPrice().getValue()).isEqualTo(46_900);
        assertThat(order.getDiscountedPrice()).isEqualTo(order.getOriginalPrice());
    }

    @DisplayName("총 금액이 5만원 이상인 주문에는 2천원 할인이 적용된다")
    @Test
    void createOrder_price50000OrMore_2000discount() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final List<CartItem> items = List.of(유자_민트_티_ID_1_5개_29500원(member), 자몽_허니_블랙티_ID_2_7개_39900원(member));
        final CartItems cartItems = new CartItems(items);
        final FixedDiscountPolicy discountPolicy = FixedDiscountPolicy.from(cartItems.sumOfPrice());

        // when
        final Order order = new Order(member, cartItems, discountPolicy);

        // then
        assertThat(order.getOriginalPrice().getValue()).isEqualTo(69_400);
        assertThat(order.getDiscountedPrice()).isEqualTo(order.getOriginalPrice().minus(2_000));
    }

    @DisplayName("총 금액이 10만원 이상인 주문에는 5천원 할인이 적용된다")
    @Test
    void createOrder_price100000OrMore_5000discount() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final List<CartItem> items = List.of(
                유자_민트_티_ID_1_5개_29500원(member),
                자몽_허니_블랙티_ID_2_7개_39900원(member),
                아메리카노_ID_3_8개_36000원(member),
                바닐라_크림_콜드브루_ID_4_3개_17400원(member));
        final CartItems cartItems = new CartItems(items);
        final FixedDiscountPolicy discountPolicy = FixedDiscountPolicy.from(cartItems.sumOfPrice());

        // when
        final Order order = new Order(member, cartItems, discountPolicy);

        // then
        assertThat(order.getOriginalPrice().getValue()).isEqualTo(122_800);
        assertThat(order.getDiscountedPrice()).isEqualTo(order.getOriginalPrice().minus(5_000));
    }

    @DisplayName("총 금액이 20만원 이상인 주문에는 1만2천원 할인이 적용된다")
    @Test
    void createOrder_price200000OrMore_12000discount() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final List<CartItem> items = List.of(맥북_ID_5_1개_1500000원(member));
        final CartItems cartItems = new CartItems(items);
        final FixedDiscountPolicy discountPolicy = FixedDiscountPolicy.from(cartItems.sumOfPrice());

        // when
        final Order order = new Order(member, cartItems, discountPolicy);

        // then
        assertThat(order.getOriginalPrice().getValue()).isEqualTo(1_500_000);
        assertThat(order.getDiscountedPrice()).isEqualTo(order.getOriginalPrice().minus(12_000));
    }
}
