package cart.domain;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final LocalDateTime timeStamp;
    private final Member member;
    private final List<OrderProduct> orderProducts;

    public Order(final Long id, final LocalDateTime timeStamp, final Member member,
                 final List<OrderProduct> orderProducts) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.member = member;
        this.orderProducts = orderProducts;
    }

    public static Order of(final Member member, final List<CartItem> cartItems) {
        validateSameMember(member, cartItems);
        List<OrderProduct> orderProducts = cartItems.stream()
                .map(cartItem -> new OrderProduct(cartItem.getProduct(), new Quantity(cartItem.getQuantity())))
                .collect(toList());
        return new Order(null, LocalDateTime.now(), member, orderProducts);
    }

    private static void validateSameMember(final Member member, final List<CartItem> cartItems) {
        if (isCartItemsNotMatchMember(member, cartItems)) {
            throw new IllegalArgumentException("장바구니 품목을 담은 멤버와 주문 멤버가 동일하지 않습니다.");
        }
    }

    private static boolean isCartItemsNotMatchMember(final Member member, final List<CartItem> cartItems) {
        return !cartItems.stream()
                .allMatch(cartItem -> cartItem.getMember().equals(member));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }
}
