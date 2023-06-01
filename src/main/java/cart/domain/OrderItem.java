package cart.domain;

import cart.dto.OrderCartItemDto;

public class OrderItem {

    private final Long id;
    private final CartOrder cartOrder;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderItem(final Long id, final CartOrder cartOrder, final String name, final int price, final String imageUrl, final int quantity) {
        this.id = id;
        this.cartOrder = cartOrder;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItem of(final CartOrder cartOrder, final OrderCartItemDto orderCartItemDto, final int quantity) {
        return new OrderItem(
                null,
                cartOrder,
                orderCartItemDto.getOrderCartItemName(),
                orderCartItemDto.getOrderCartItemPrice(),
                orderCartItemDto.getOrderCartItemImageUrl(),
                quantity
        );
    }

    public Long getId() {
        return id;
    }

    public CartOrder getCartOrder() {
        return cartOrder;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
