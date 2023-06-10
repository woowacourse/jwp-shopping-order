package cart.domain.cart;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Nickname;
import cart.domain.member.Password;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CartItemTest {
    private CartItem cartItem;
    private Member member;
    private Product product;

    @BeforeEach
    public void setUp() {
        member = new Member(1L, new Email("a@a.aa"), new Password("1234"), new Nickname("루카"));
        product = new Product("테스트", 1000, "이미지 주소");
        cartItem = new CartItem(member, product);
    }

    @Test
    public void 주문내역을_생성한다() {
        OrderItem orderItem = cartItem.toOrderItem();

        assertThat(orderItem).isNotNull();
    }

    @Test
    public void 가격을_계산한다() {
        int totalPrice = cartItem.calculatePrice();

        assertThat(totalPrice).isEqualTo(1000);
    }

    @Test
    public void 장바구니_수량이_달라지면_예외를_발생한다() {
        CartItem otherCartItem = new CartItem(null, 2, product, member);

        assertThatThrownBy(() -> cartItem.checkValue(otherCartItem))
                .isInstanceOf(CartItemException.QuantityIncorrect.class);
    }

    @Test
    public void 장바구니_수량이_맞는지_확인한다() {
        CartItem otherCartItem = new CartItem(null, 1, product, member);

        assertDoesNotThrow(() -> cartItem.checkValue(otherCartItem));
    }

    @Test
    public void 멤버가_다르면_예외를_발생한다() {
        Member otherMember = new Member(2L, new Email("b@a.aa"), new Password("1234"), new Nickname("루카2"));

        assertThatThrownBy(() -> cartItem.checkOwner(otherMember))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    public void 멤버가_일치하는지_확인한다() {
        assertDoesNotThrow(() -> cartItem.checkOwner(member));
    }

    @Test
    public void 수량을_변경한다() {
        int expected = 5;
        CartItem changedCartItem = cartItem.changeQuantity(expected);

        assertThat(changedCartItem.getQuantity()).isEqualTo(expected);
    }

}
