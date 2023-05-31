package cart.dto;

import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    
    private Long id;
    private List<OrderItemResponse> orderItemResponses;
    private Integer productPrice;
    private Integer discountPrice;
    private Integer deliveryFee;
    private Integer totalPrice;
    
    
    public OrderResponse(Long id, List<OrderItemResponse> orderItemResponses, Integer productPrice,
            Integer discountPrice,
            Integer deliveryFee, Integer totalPrice) {
        this.id = id;
        this.orderItemResponses = orderItemResponses;
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
    
    public Long getId() {
        return id;
    }
    
    public List<OrderItemResponse> getOrderItemResponses() {
        return orderItemResponses;
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
