package cart.dto.response;

import cart.domain.OrderItem;

public class OrderItemDetailResponse {

    private String name;
    private String imageUrl;
    private Integer quantity;
    private Integer price;

    public OrderItemDetailResponse() {
    }

    public OrderItemDetailResponse(final String name, final String imageUrl, final Integer quantity,
                                   final Integer price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItemDetailResponse of(OrderItem orderItem) {
        return new OrderItemDetailResponse(orderItem.getName(), orderItem.getImageUrl(), orderItem.getQuantity(),
                orderItem.getPrice());
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }
}
