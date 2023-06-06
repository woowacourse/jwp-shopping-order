package cart.domain;

import cart.entity.OrderEntity;
import cart.exception.CartItemException.IllegalMember;
import cart.exception.CartItemException.TotalPriceNotSame;

import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final long shippingFee;
    private final long totalProductsPrice;
    private final long usedPoint;
    private final List<OrderItem> orderItems;
    private final String createdAt;

    public Order(
            final Long id,
            final Member member,
            final long shippingFee,
            final long totalProductsPrice,
            final long usedPoint,
            final List<OrderItem> orderItems,
            final String createdAt
    ) {
        this.id = id;
        this.member = member;
        this.shippingFee = shippingFee;
        this.totalProductsPrice = totalProductsPrice;
        this.usedPoint = usedPoint;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    public static Order of(final Member member, long shippingFee, long usedPoint, final List<OrderItem> orderItems) {
        long totalProductPrice = orderItems.stream()
                .mapToLong(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        return new Order(null, member, shippingFee, totalProductPrice, usedPoint, orderItems, null);
    }

    public static Order of(final Member member, final OrderEntity orderEntity, final List<OrderItem> orderItems) {
        return new Order(
                orderEntity.getId(),
                member,
                orderEntity.getShippingFee(),
                orderEntity.getTotalProductsPrice(),
                orderEntity.getUsedPoint(),
                orderItems,
                orderEntity.getCreatedAt()
        );
    }

    public OrderEntity toEntity() {
        return OrderEntity.of(member.getId(), shippingFee, totalProductsPrice, usedPoint);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public long getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void checkPrice(final long totalPrice) {
        if (totalProductsPrice + shippingFee != totalPrice) {
            throw new TotalPriceNotSame();
        }
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMember();
        }
    }

    public void checkTotalProductsPrice(final long totalProductsPrice) {
        if (this.totalProductsPrice != totalProductsPrice) {
            throw new TotalPriceNotSame();
        }
    }

    public void checkShippingFee(final long shippingFee) {
        if (this.shippingFee != shippingFee) {
            throw new TotalPriceNotSame();
        }
    }

    public long getUsedPoint() {
        return usedPoint;
    }

    public long getPayment() {
        return totalProductsPrice + shippingFee - usedPoint;
    }
}
