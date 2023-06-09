package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {
    
    private final Long id;
    private final Long memberId;
//    private final List<OrderItem> orderItems;
    private final OrderItems orderItems;
    private final int deliveryFee;
    private final int discountPrice;
    private final LocalDateTime createdAt;
    
//    public Order(Long id, Long memberId, List<OrderItem> orderItems, int deliveryFee, int discountPrice,
//            LocalDateTime createdAt) {
//        this.id = id;
//        this.memberId = memberId;
//        this.orderItems = new ArrayList<>(orderItems);
//        this.deliveryFee = deliveryFee;
//        this.discountPrice = discountPrice;
//        this.createdAt = createdAt;
//    }
    
    public Order(Long id, Long memberId, OrderItems orderItems, int deliveryFee, int discountPrice,
            LocalDateTime createdAt) {
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
        return orderItems.getTotalPrice();
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
    
    public OrderItems getOrderItems() {
        return orderItems;
    }
    
    public int getDeliveryFee() {
        return deliveryFee;
    }
    
    public int getDiscountPrice() {
        return discountPrice;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
