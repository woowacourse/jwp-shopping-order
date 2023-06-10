package cart.domain.cart;

import cart.domain.coupon.AmountCoupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.UsableMemberCoupon;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Nickname;
import cart.domain.member.Password;
import cart.domain.order.Order;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CartItemsTest {
    private CartItems cartItems;
    private Member member;
    private MemberCoupon memberCoupon;

    @BeforeEach
    public void setUp() {
        member = new Member(1L, new Email("a@a.aa"), new Password("1234"), new Nickname("루카"));
        memberCoupon = new UsableMemberCoupon(1L, new AmountCoupon(new CouponInfo(1L, "1000원 쿠폰", 3000, 1000), 1000), member, LocalDateTime.MAX, LocalDateTime.now());
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, 2, new Product(1L,"Product 1", 10, "image-url"), member));
        cartItems.add(new CartItem(2L, 1, new Product(2L,"Product 2", 20, "image-url"), member));
        cartItems.add(new CartItem(3L, 3, new Product(3L,"Product 3", 30, "image-url"), member));
        this.cartItems = new CartItems(cartItems);
    }

    @Test
    public void 전체_가격을_조회한다() {
        int totalPrice = cartItems.calculateTotalPrice();
        assertThat(totalPrice).isEqualTo(130);
    }

    @Test
    public void 아이디로_카트_필터링한다() {
        List<Long> ids = List.of(1L, 3L);
        CartItems filteredItems = cartItems.filterByIds(ids);

        Assertions.assertAll(
            () -> assertThat(filteredItems.getCartItems()).hasSize(2),
            () -> assertThat(filteredItems.getCartItems().get(0).getQuantity()).isEqualTo(2),
            () -> assertThat(filteredItems.getCartItems().get(1).getQuantity()).isEqualTo(3)
        );
    }

    @Test
    public void 카트_상태가_일치하는지_확인한다() {
        CartItems other = new CartItems(new ArrayList<>(cartItems.getCartItems()));
        assertDoesNotThrow(() -> cartItems.checkStatus(other, member));
    }

    @Test
    public void 멤버가_일치하지_않으면_예외를_발생한다() {
        Member differentMember = new Member(2L, new Email("b@a.aa"), new Password("1234"), new Nickname("루카2"));
        CartItems other = new CartItems(new ArrayList<>(cartItems.getCartItems()));
        assertThatThrownBy(() -> cartItems.checkStatus(other, differentMember))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }

    @Test
    public void 카트_수량이_일치하지_않으면_예외를_발생한다() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem(1L, 2, new Product(1L,"Product 1", 10, "image-url"), member));
        cartItemList.add(new CartItem(2L, 2, new Product(2L,"Product 2", 20, "image-url"), member));
        cartItemList.add(new CartItem(3L, 3, new Product(3L, "Product 3", 30, "image-url"), member));
        CartItems other = new CartItems(cartItemList);
        assertThatThrownBy(() -> cartItems.checkStatus(other, member))
                .isInstanceOf(CartItemException.QuantityIncorrect.class);
    }

    @Test
    public void 장바구니가_비었는지_확인한다() {
        assertDoesNotThrow(cartItems::checkNotEmpty);
    }

    @Test
    public void 장바구니가_비어있으면_예외를_빨생한다() {
        CartItems emptyCartItems = new CartItems(new ArrayList<>());
        assertThatThrownBy(emptyCartItems::checkNotEmpty)
                .isInstanceOf(CartItemException.EmptyCart.class);
    }

    @Test
    public void 주문한다() {
        Order order = cartItems.order(member, memberCoupon);
        assertThat(order).isNotNull();
    }

    @Test
    public void 장바구니에_중복된_상품이_있으면_예외를_발생한다() {
        assertThatThrownBy(() -> cartItems.checkDuplicated(1L))
                .isInstanceOf(CartItemException.AlreadyExist.class);
    }
}
