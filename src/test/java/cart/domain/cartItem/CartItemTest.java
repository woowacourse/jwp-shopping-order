package cart.domain.cartItem;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.junit.jupiter.api.Test;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CartItemTest {

    private Member 하디_멤버 = new Member(1L, "hardy", "1234");
    private Member 현구막_멤버 = new Member(2L, "hardy", "1234");
    private CartItem 하디_피자_장바구니_아이템 = new CartItem(3L, 하디_멤버, 피자);
    private CartItem 하디_샐러드_장바구니_아이템 = new CartItem(1L, 하디_멤버, 샐러드);
    private CartItem 하디_치킨_장바구니_아이템 = new CartItem(1000L, 하디_멤버, 치킨);

    @Test
    void 장바구니_아이템의_멤버가_일치하지_않으면_예외를_던진다() {
        // given

        // when, then
        assertThatThrownBy(() -> 하디_피자_장바구니_아이템.checkOwner(현구막_멤버))
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.ILLEGAL_MEMBER);
                });
        ;
    }

    @Test
    void 장바구니_아이템의_멤버가_일치하면_예외를_던지지않는다() {
        // given

        // when, then
        하디_피자_장바구니_아이템.checkOwner(하디_멤버);
    }

    @Test
    void 장바구니_아이템_수량보다_재고가_적으면_예외를_던진다() {
        // given

        // when, then
        assertThatThrownBy(() -> 하디_치킨_장바구니_아이템.checkQuantity())
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_QUANTITY_EXCESS);
                });
    }

    @Test
    void 장바구니_아이템_수량보다_재고가_많거나_같으면_예외를_던지지_않는다() {
        // given

        // when, then
        하디_피자_장바구니_아이템.checkQuantity();
    }

    @Test
    void 장바구니_아이템의_수량을_변경할_수_있다() {
        // given
        Long updateQuantity = 200L;

        // when
        하디_피자_장바구니_아이템.changeQuantity(updateQuantity);

        // then
        assertThat(하디_피자_장바구니_아이템.getQuantity()).isEqualTo(updateQuantity);
    }
}
