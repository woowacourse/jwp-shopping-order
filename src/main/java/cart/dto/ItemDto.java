package cart.dto;

import cart.domain.cart.Item;
import cart.domain.cart.Product;

public class ItemDto {

    private final Long orderItemId;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final int quantity;

    public ItemDto(
            final Long orderItemId,
            final String name,
            final long price,
            final String imageUrl,
            final int quantity
    ) {
        this.orderItemId = orderItemId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static ItemDto from(final Item item) {
        final Product product = item.getProduct();
        return new ItemDto(
                item.getId(),
                product.getName(),
                product.getPrice().getLongValue(),
                product.getImageUrl(),
                item.getQuantity()
        );
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
