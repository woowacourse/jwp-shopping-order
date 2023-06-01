package cart.application.response;

import cart.domain.order.OrderItem;

public class OrderItemResponse {

    private Long productId;
    private String name;
    private int quantity;
    private int price;
    private int totalPrice;

    private OrderItemResponse(Long productId, String name, int quantity, int price, int totalPrice) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName().getValue(),
                orderItem.getQuantity().getValue(),
                orderItem.getProduct().getPrice().getValue().intValue(),
                orderItem.totalPrice().getValue().intValue()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
