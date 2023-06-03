package cart.domain;

import cart.exception.CartItemException;
import cart.exception.IllegalMemberException;
import cart.exception.OrderException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private static final int MAXIMUM_QUANTITIES_SUM = 99;

    private final Long id;
    private final List<OrderItem> orderItems;
    private final Member member;
    private final long priceAfterDiscount;
    private final Timestamp orderTime;

    private Order(Long id, List<OrderItem> orderItems, Member member, long priceAfterDiscount, Timestamp orderTime) {
        checkQuantityLimit(orderItems);
        checkItemExist(orderItems);
        this.id = id;
        this.orderItems = orderItems;
        this.member = member;
        this.priceAfterDiscount = priceAfterDiscount;
        this.orderTime = orderTime;
    }

    public static Order of(Long id, List<OrderItem> orderItems, Member member, long priceAfterDiscount, Timestamp orderTime) {
        return new Order(id, orderItems, member, priceAfterDiscount, orderTime);
    }

    public static Order of(List<CartItem> cartItems, Member member, long priceAfterDiscount) {
        final List<OrderItem> orderItems = cartItems.stream()
                .map(OrderItem::from)
                .collect(Collectors.toList());

        return of(null, orderItems, member, priceAfterDiscount, null);
    }

    private void checkQuantityLimit(List<OrderItem> orderItems) {
        if (sumQuantities(orderItems) > MAXIMUM_QUANTITIES_SUM) {
            throw new CartItemException.InvalidQuantity("한 번에 최대 " + MAXIMUM_QUANTITIES_SUM + "개까지 주문이 가능합니다");
        }
    }

    private void checkItemExist(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            throw new OrderException.EmptyCart();
        }
    }

    private int sumQuantities(List<OrderItem> cartItems) {
        return cartItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public void checkOwner(Member other) {
        if (!member.equals(other)) {
            throw new IllegalMemberException("접근할 수 없는 자원입니다. 현재 사용자: " + member.getId() + " 접근하려는 사용자: " + other.getId());
        }
    }

    public long getPriceBeforeDiscount() {
        return orderItems.stream()
                .mapToLong(OrderItem::getPrice)
                .sum();
    }

    public long getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public Member getOrderingMember() {
        return member;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }
}
