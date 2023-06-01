package cart.domain;

import cart.exception.CartItemException;
import cart.exception.OrderException;

import java.time.OffsetDateTime;
import java.util.List;

public class Order {

    private static final int MAXIMUM_QUANTITIES_SUM = 99;

    private final Long id;
    private final List<CartItem> cartItems;
    private final OffsetDateTime orderTime;

    private Order(Long id, List<CartItem> cartItems, OffsetDateTime orderTime) {
        checkQuantityLimit(cartItems);
        checkItemExist(cartItems);
        this.id = id;
        this.cartItems = cartItems;
        this.orderTime = orderTime;
    }

    public static Order of(Long id, List<CartItem> cartItems, Member member, OffsetDateTime orderTime) {
        checkOwner(cartItems, member);

        return new Order(id, cartItems, orderTime);
    }

    public static Order of(Member member, List<CartItem> cartItems) {
        return of(null, cartItems, member, null);
    }

    private static void checkOwner(List<CartItem> cartItems, Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    private void checkQuantityLimit(List<CartItem> cartItems) {
        if (sumQuantities(cartItems) > MAXIMUM_QUANTITIES_SUM) {
            throw new CartItemException.InvalidQuantity("한 번에 최대 " + MAXIMUM_QUANTITIES_SUM + "개까지 주문이 가능합니다");
        }
    }

    private void checkItemExist(List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new OrderException.EmptyCart();
        }
    }

    private int sumQuantities(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public long getPricesSum() {
        return cartItems.stream()
                .mapToLong(CartItem::getPrice)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public OffsetDateTime getOrderTime() {
        return orderTime;
    }

    public Member getOrderingMember() {
        return cartItems.get(0).getMember();
    }
}
