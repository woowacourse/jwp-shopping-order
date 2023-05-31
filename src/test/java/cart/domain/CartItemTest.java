package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.exception.CartItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @DisplayName("id와 CartItem으로 객체를 만드면 id를 제외한 항목이 복사된다.")
    @Test
    void constructWithOther() {
        final CartItem cartItem = new CartItem(null, null, null, 0);
        final CartItem result = new CartItem(1L, cartItem);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getMember()).isNull(),
                () -> assertThat(result.getProduct()).isNull(),
                () -> assertThat(result.getQuantity()).isZero()
        );
    }

    @DisplayName("id와 수량 없이 객체를 만드면 id는 null, quantity는 1인 객체를 생성한다.")
    @Test
    void constructWithNoIdAndNoQuantity() {
        final CartItem result = new CartItem(new Member(1L, null, null), null);
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getMember().getId()).isEqualTo(1L),
                () -> assertThat(result.getProduct()).isNull(),
                () -> assertThat(result.getQuantity()).isEqualTo(1)
        );
    }

    @DisplayName("장바구니 품목의 Member id와 인자의 Member id가 같으면 예외를 발생시키지 않는다.")
    @Test
    void checkOwner() {
        final CartItem cartItem = new CartItem(null, new Member(1L, null, null), null, 0);
        assertThatCode(() -> cartItem.checkOwner(new Member(1L, null, null)))
                .doesNotThrowAnyException();
    }

    @DisplayName("장바구니 품목의 Member id와 인자의 Member id가 같으면 예외를 발생시키지 않는다.")
    @Test
    void checkOwnerThrowExceptionWhenIllegalMember() {
        final CartItem cartItem = new CartItem(null, new Member(1L, null, null), null, 0);
        final Member member = new Member(2L, null, null);
        assertThatThrownBy(() -> cartItem.checkOwner(member))
                .isInstanceOf(CartItemException.IllegalMember.class)
                .hasMessage(new CartItemException.IllegalMember(cartItem, member).getMessage());
    }

    @Test
    void changeQuantity() {
        final CartItem cartItem = new CartItem(null, null, null, 0);
        final CartItem result = cartItem.changeQuantity(10);
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getMember()).isNull(),
                () -> assertThat(result.getProduct()).isNull(),
                () -> assertThat(result.getQuantity()).isEqualTo(10)
        );
    }
}
