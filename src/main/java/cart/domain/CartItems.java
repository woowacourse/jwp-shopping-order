package cart.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::calculatePrice)
                .sum();
    }

    public CartItems filterByIds(final List<Long> ids) {
        List<CartItem> selectedCartItems = ids.stream()
                .map(this::selectById)
                .collect(Collectors.toList());
        return new CartItems(selectedCartItems);
    }

    // TODO: 6/4/23 이걸 private 하게 바꿔도 될듯? 
    public void checkStatus(final CartItems other, final Member member) {
        for (CartItem cartItem : other.getCartItems()) {
            cartItem.checkOwner(member);
            validateValue(cartItem, other.getCartItems());
        }
    }

    private void validateValue(final CartItem currentCartItem, final List<CartItem> otherCartItems) {
        CartItem selectedCartItem = otherCartItems.stream()
                .filter(cartItem -> cartItem.equals(currentCartItem))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 상품은 선택되지 않았습니다."));
        currentCartItem.checkValue(selectedCartItem);
    }

    private CartItem selectById(final Long id) {
        return cartItems.stream()
                .filter(cartItem -> Objects.equals(cartItem.getId(), id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 카트목록이 존재하지 않습니다."));
    }

    public Order order(final Member member, final MemberCoupon memberCoupon) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(CartItem::toOrderItem)
                .collect(Collectors.toList());
        return Order.of(null, orderItems, member, memberCoupon);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItems cartItems1 = (CartItems) o;
        return Objects.equals(cartItems, cartItems1.cartItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItems);
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItems=" + cartItems +
                '}';
    }
}
