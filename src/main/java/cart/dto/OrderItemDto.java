package cart.dto;

import cart.domain.CartOrder;

public class OrderItemDto {

    private final Long id;
    private final CartOrder cartOrder;
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderItemDto(final Long id, final CartOrder cartOrder, final Long productId, final String name, final int price, final String imageUrl, final int quantity) {
        this.id = id;
        this.cartOrder = cartOrder;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemDto of(final CartOrder cartOrder, final Long productId, final OrderCartItemDto orderCartItemDto, final int quantity) {
        return new OrderItemDto(
                null,
                cartOrder,
                productId,
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

    public Long getProductId() {
        return productId;
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
