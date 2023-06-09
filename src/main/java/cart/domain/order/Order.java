package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.product.Price;
import cart.exception.OrderException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private static final int MIN_ORDER_ITEM_SIZE = 1;
    private final Long id;
    private final Member member;
    private final Timestamp orderTime;
    private final List<OrderItem> orderItems;

    public Order(final Member member, final List<OrderItem> orderItemList) {
        this(null, member, null, orderItemList);
    }

    public Order(
            final Long id,
            final Member member,
            final Timestamp orderTime,
            final List<OrderItem> orderItemList
    ) {
        this.id = id;
        this.member = member;
        this.orderTime = orderTime;
        this.orderItems = new ArrayList<>(orderItemList);

        validateOrderItemCount(this.orderItems);
    }

    private void validateOrderItemCount(final List<OrderItem> orderItems) {
        if (orderItems.size() < MIN_ORDER_ITEM_SIZE) {
            throw new IllegalArgumentException("주문 상품은 최소 " + MIN_ORDER_ITEM_SIZE + "개 이상이어야 합니다.");
        }
    }

    public Price getTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Price.minPrice(), Price::add);
    }

    public void checkOwner(final Member member) {
        if (this.member.isNotSameMember(member)) {
            throw new OrderException.IllegalMember(member);
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }
}
