package cart.domain;

import java.util.List;
import java.util.Objects;

public class Order {
    
    private final Long id;
    private final Long memberId;
    private final List<OrderItem> orderItems;
    private final int deliveryFee;
    private final int discountPrice;
    private final String createdAt;
    
    public Order(Long id, Long memberId, List<OrderItem> orderItems, int deliveryFee, int discountPrice,
            String createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.discountPrice = discountPrice;
        this.createdAt = createdAt;
    }
    
    public int getTotalPrice() {
        int productPrice = getProductPrice();
        return productPrice + deliveryFee - discountPrice;
    }
    
    public int getProductPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
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
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getMemberId() {
        return memberId;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public int getDeliveryFee() {
        return deliveryFee;
    }
    
    public int getDiscountPrice() {
        return discountPrice;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
}
