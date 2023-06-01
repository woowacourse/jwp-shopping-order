package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.exception.customexception.IllegalMemberException;
import cart.exception.customexception.OrderTotalPriceIsNotMatchedException;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    private Member 하디_멤버 = new Member(1L, "hardy", "1234");
    private Member 현구막_멤버 = new Member(2L, "hardy", "1234");
    private CartItem 하디_피자_장바구니_아이템 = new CartItem(3L, 하디_멤버, 피자);
    private CartItem 하디_샐러드_장바구니_아이템 = new CartItem(1L, 하디_멤버, 샐러드);
    private CartItem 하디_치킨_장바구니_아이템 = new CartItem(1L, 하디_멤버, 치킨);
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private Long correctTotalPrice = 69000L;
    private Long wrongTotalPrice = 9999L;
    private Order order;

    @Test
    void 주문의_멤버가_다른_멤버이면_예외가_발생한다() {
        // given
        order = new Order(하디_멤버, 3000L, 0L, timestamp);
        List<CartItem> cartItems = List.of(하디_피자_장바구니_아이템, 하디_샐러드_장바구니_아이템, 하디_치킨_장바구니_아이템);
        order.addOrderItems(cartItems);

        // when, then
        assertThatThrownBy(() -> order.checkOwner(현구막_멤버))
                .isInstanceOf(IllegalMemberException.class);
    }

    @Test
    void 주문의_멤버가_일치하면_예외가_발생하지않는다() {
        // given
        order = new Order(하디_멤버, 3000L, 0L, timestamp);
        List<CartItem> cartItems = List.of(하디_피자_장바구니_아이템, 하디_샐러드_장바구니_아이템, 하디_치킨_장바구니_아이템);
        order.addOrderItems(cartItems);

        // when, then
        order.checkOwner(하디_멤버);
    }

    @Test
    void 주문_아이템의_가격의_총합이_일치하지_않으면_예외가_발생한다() {
        // given
        order = new Order(하디_멤버, 3000L, 0L, timestamp);
        List<CartItem> cartItems = List.of(하디_피자_장바구니_아이템, 하디_샐러드_장바구니_아이템, 하디_치킨_장바구니_아이템);
        order.addOrderItems(cartItems);

        // when, then
        assertThatThrownBy(() -> order.checkTotalPrice(wrongTotalPrice))
                .isInstanceOf(OrderTotalPriceIsNotMatchedException.class);
    }

    @Test
    void 주문_아이템의_가격의_총합이_일치하면_예외가_발생하지않는다() {
        // given
        order = new Order(하디_멤버, 3000L, 0L, timestamp);
        List<CartItem> cartItems = List.of(하디_피자_장바구니_아이템, 하디_샐러드_장바구니_아이템, 하디_치킨_장바구니_아이템);
        order.addOrderItems(cartItems);

        // when, then
        order.checkTotalPrice(correctTotalPrice);
    }
}
