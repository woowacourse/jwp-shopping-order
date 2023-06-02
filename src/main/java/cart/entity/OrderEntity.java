package cart.entity;

import cart.domain.Member;
import cart.domain.order.DiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int originalPrice;
    private final int discountedPrice;
    private final LocalDateTime createdAt;

    public OrderEntity(final Long id,
                       final Long memberId,
                       final int originalPrice,
                       final int discountedPrice,
                       final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.createdAt = createdAt;
    }

    public static OrderEntity from(Order order) {
        return new OrderEntity(
                null,
                order.getMember().getId(),
                order.getOriginalPrice().getValue(),
                order.getDiscountedPrice().getValue(),
                null
        );
    }

    public Order toDomain(Member member, OrderItems orderItems, DiscountPolicy discountPolicy) {
        return new Order(this.id, member, orderItems, discountPolicy, this.createdAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}