package cart.controller.dto.response;

import cart.domain.OrderItem;

public class OrderItemResponse {

    private final long id;
    private final String name;
    private final int singleProductPrice;
    private final int count;
    private final String imageUrl;

    private OrderItemResponse(final long id, final String name, final int singleProductPrice, final int count, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.singleProductPrice = singleProductPrice;
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

    public int getSingleProductPrice() {
        return singleProductPrice;
    }

    public int getCount() {
        return count;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
