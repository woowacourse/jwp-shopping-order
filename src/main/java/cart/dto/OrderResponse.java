package cart.dto;

import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    
    private Long orderId;
    private List<OrderItemResponse> items;
    private Integer productPrice;
    private Integer discountPrice;
    private Integer deliveryFee;
    private Integer totalPrice;
    
    
    public OrderResponse(Long orderId, List<OrderItemResponse> items, Integer productPrice,
            Integer discountPrice,
            Integer deliveryFee, Integer totalPrice) {
        this.orderId = orderId;
        this.items = items;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
    }
    
    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::of)
                        .collect(Collectors.toList()),
                order.getProductPrice(),
                order.getDiscountPrice(),
                order.getDeliveryFee(),
                order.getTotalPrice()
        );
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public List<OrderItemResponse> getItems() {
        return items;
    }
    
    public Integer getProductPrice() {
        return productPrice;
    }
    
    public Integer getDiscountPrice() {
        return discountPrice;
    }
    
    public Integer getDeliveryFee() {
        return deliveryFee;
    }
    
    public Integer getTotalPrice() {
        return totalPrice;
    }
}
