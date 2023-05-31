package cart.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private Member member;
    private List<OrderItem> orderItems;
    private Point spendPoint;
    private LocalDateTime createdAt;

    public Order(Long id, Member member, List<OrderItem> orderItems, long spendPoint, LocalDateTime createdAt) {
        validateOverPrice(orderItems, spendPoint);
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.spendPoint = new Point(spendPoint);
        this.createdAt = createdAt;
    }

    private void validateOverPrice(List<OrderItem> orderItems, long spendPoint) {
        if (hasOverPrice(orderItems, spendPoint)) {
            throw new IllegalArgumentException(); // TODO
        }
    }

    private boolean hasOverPrice(List<OrderItem> orderItems, long spendPoint) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(Price.ZERO, Price::plus).getAmount() < spendPoint;
    }

    public String getThumbnailUrl() {
        return orderItems.stream()
                .map(OrderItem::getProduct)
                .map(Product::getImageUrl)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new); // TODO
    }

    public Price calculateTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(Price.ZERO, Price::plus);
    }

    public Price calculateSpendPrice() {
        Price totalPrice = calculateTotalPrice();
        return totalPrice.minus(Price.from(spendPoint.getAmount()));
    }

    public Point calculateRewardPoint(double percent) {
        Price spendPrice = calculateSpendPrice();
        long amount = spendPrice.getAmount();
        double reward = amount * (percent / 100);
        return new Point((long) Math.ceil(reward));
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public Point getSpendPoint() {
        return spendPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new IllegalArgumentException(); // TODO
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
