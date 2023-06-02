package cart.domain;

import cart.exception.IllegalOrderException;
import cart.exception.NumberRangeException;
import cart.exception.UnauthorizedAccessException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private static final String EMPTY_IMAGE_URL = "";

    private final Long id;
    private final Member member;
    private final List<OrderItem> orderItems;
    private final Point spendPoint;
    private final LocalDateTime createdAt;

    public Order(Long id, Member member, List<OrderItem> orderItems, long spendPoint, LocalDateTime createdAt) {
        validateEmptyOrderItems(orderItems);
        validateOverPrice(orderItems, spendPoint);
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.spendPoint = new Point(spendPoint);
        this.createdAt = createdAt;
    }

    private void validateEmptyOrderItems(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            throw new IllegalOrderException("주문할 상품이 존재하지 않습니다.");
        }
    }

    private void validateOverPrice(List<OrderItem> orderItems, long spendPoint) {
        if (hasOverPrice(orderItems, spendPoint)) {
            throw new NumberRangeException("point", "포인트는 총 주문 가격보다 클 수 없습니다.");
        }
    }

    private boolean hasOverPrice(List<OrderItem> orderItems, long spendPoint) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(Price.ZERO, Price::plus).getAmount() < spendPoint;
    }

    public String getFirstProductName() {
        return orderItems.stream()
                .map(OrderItem::getProduct)
                .map(Product::getName)
                .findFirst()
                .orElseThrow(() -> new IllegalOrderException("해당 주문 상품을 찾을 수 없습니다."));
    }

    public int getOrderItemCount() {
        return orderItems.size();
    }

    public String getThumbnailUrl() {
        return orderItems.stream()
                .map(OrderItem::getProduct)
                .map(Product::getImageUrl)
                .findFirst()
                .orElse(EMPTY_IMAGE_URL);
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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new UnauthorizedAccessException("해당 회원의 주문이 아닙니다.");
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
}
