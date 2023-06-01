package cart.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Long id;
    private final List<OrderProduct> orderProducts;
    private final Member member;
    private final DeliveryFee deliveryFee;
    private final Point savedPoint;
    private final Point usedPoint;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Order(final Long id, final List<OrderProduct> orderProducts, final Member member,
                 final DeliveryFee deliveryFee, final Point usedPoint,
                 final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.member = member;
        this.deliveryFee = deliveryFee;
        this.savedPoint = Point.fromTotalPrice(getTotalPrice());
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order(final Long id, final List<OrderProduct> orderProducts, final Member member, final int usedPoint) {
        this(id, orderProducts, member, calculateDeliveryFee(orderProducts), new Point(usedPoint), null, null);
    }

    private static DeliveryFee calculateDeliveryFee(List<OrderProduct> orderProducts) {
        int totalPrice = orderProducts.stream()
                .mapToInt(orderProduct -> orderProduct.getProduct().getPrice() * orderProduct.getQuantity())
                .sum();

        if (totalPrice < 50000) {
            return DeliveryFee.DEFAULT;
        }
        return DeliveryFee.FREE;
    }

    public int getTotalPrice() {
        return orderProducts.stream()
                .mapToInt(orderProduct -> orderProduct.getProduct().getPrice() * orderProduct.getQuantity())
                .sum();
    }

    public boolean isOwner(Member member) {
        if (member.getId() == null || this.member.getId() == null) {
            return false;
        }

        return this.member.equals(member);
    }

    public Long getId() {
        return id;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Member getMember() {
        return member;
    }

    public int getDeliveryFee() {
        return deliveryFee.getValue();
    }

    public int getSavedPoint() {
        return savedPoint.getValue();
    }

    public int getUsedPoint() {
        return usedPoint.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
