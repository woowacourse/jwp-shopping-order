package cart.domain;

import static cart.fixtures.CartItemFixtures.바닐라_크림_콜드브루_ID_4_3개_17400원;
import static cart.fixtures.CartItemFixtures.유자_민트_티_ID_1_5개_29500원;
import static cart.fixtures.CartItemFixtures.자몽_허니_블랙티_ID_2_7개_39900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.order.Price;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class CartItemsTest {

    @DisplayName("다른 Member의 장바구니 상품이 포함되어 있으면 예외가 발생한다")
    @Test
    void createCartItems_containsOtherMember_throws() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final Member otherMember = new Member(2L, "other@email.com", "password");
        final List<CartItem> cartItems = List.of(유자_민트_티_ID_1_5개_29500원(member), 자몽_허니_블랙티_ID_2_7개_39900원(otherMember));

        // when, then
        assertThatThrownBy(() -> new CartItems(cartItems, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 Member의 장바구니 상품이 포함되어 있습니다");

    }

    @DisplayName("장바구니 상품이 존재하지 않으면 예외가 발생한다")
    @Test
    void createCartItems_ItemNotExist_throws() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");

        // when, then
        assertThatThrownBy(() -> new CartItems(Collections.emptyList(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니 상품이 존재하지 않습니다.");
    }

    @DisplayName("장바구니 상품들의 합산 금액을 계산한다")
    @Test
    void sumOfPrice() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final List<CartItem> items = List.of(유자_민트_티_ID_1_5개_29500원(member), 바닐라_크림_콜드브루_ID_4_3개_17400원(member));
        final CartItems cartItems = new CartItems(items, member);

        // when
        Price sum = cartItems.sumOfPrice();

        // then
        assertThat(sum.getValue()).isEqualTo(46_900);
    }

}
