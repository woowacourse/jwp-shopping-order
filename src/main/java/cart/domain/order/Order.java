package cart.domain.order;

import cart.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;
    private final DiscountPolicy discountPolicy;
    private final LocalDateTime createdAt;

    public Order(final Member member, final OrderItems orderItems, final DiscountPolicy discountPolicy) {
        this(null, member, orderItems, discountPolicy, null);
    }

    public Order(final Long id, final Member member, final OrderItems orderItems, final DiscountPolicy discountPolicy) {
        this(id, member, orderItems, discountPolicy, null);
    }

    public Order(final Long id, final Member member, final OrderItems orderItems, final DiscountPolicy discountPolicy,
                 final LocalDateTime createdAt) {
        validateMember(member, orderItems);
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.discountPolicy = discountPolicy;
        this.createdAt = createdAt;
    }

    private void validateMember(final Member member, final OrderItems orderItems) {
        if (!member.equals(orderItems.getMember())) {
            throw new IllegalArgumentException("주문자가 주문하지 않은 상품이 포함되어 있습니다");
        }
    }

    public Price getOriginalPrice() {
        return orderItems.sumOfPrice();
    }

    public Price getDiscountedPrice() {
        final Price originalPrice = orderItems.sumOfPrice();
        return discountPolicy.discount(originalPrice);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getOrderItems();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
