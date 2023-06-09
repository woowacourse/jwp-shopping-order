package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private final Member member;
    private final Long usedPoint;
    private final Long earnedPoint;
    private final Timestamp createdAt;
    private final List<OrderItem> orderItems;

    public Order(Member member, Long usedPoint, Long earnedPoint, Timestamp createdAt) {
        this.member = member;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
        this.createdAt = createdAt;
        this.orderItems = new ArrayList<>();
    }

    public Order(Long id, Member member, Long usedPoint, Long earnedPoint, Timestamp createdAt) {
        this.id = id;
        this.member = member;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
        this.createdAt = createdAt;
        this.orderItems = new ArrayList<>();
    }

    public void addOrderItems(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(cartItem.getProduct().getId(),
                    cartItem.getProduct().getName(),
                    cartItem.getProduct().getPrice(),
                    cartItem.getProduct().getImageUrl(),
                    cartItem.getQuantity());
            orderItems.add(orderItem);
        }
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartException(ErrorCode.ILLEGAL_MEMBER);
        }
    }

    public void checkTotalPrice(Long totalPrice) {
        Long actualTotalPrice = orderItems.stream()
                .mapToLong(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
        if (actualTotalPrice.equals(totalPrice)) {
            return;
        }
        throw new CartException(ErrorCode.ORDER_TOTAL_PRICE_UNMATCHED);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getEarnedPoint() {
        return earnedPoint;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
