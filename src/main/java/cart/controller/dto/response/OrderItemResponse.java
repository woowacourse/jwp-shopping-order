package cart.controller.dto.response;

import cart.domain.OrderItem;

public class OrderItemResponse {

    private final long id;
    private final String name;
    private final int price;
    private final int count;
    private final String imageUrl;

    private OrderItemResponse(final long id, final String name, final int price, final int count, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.imageUrl = imageUrl;
    }

    public static OrderItemResponse from(final OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getPrice().getAmount(),
                orderItem.getQuantity().getValue(),
                orderItem.getProduct().getImageUrl());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
