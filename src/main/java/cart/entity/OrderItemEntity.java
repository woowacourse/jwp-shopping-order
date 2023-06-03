package cart.entity;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;

public class OrderItemEntity {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final long price;
    private final Integer quantity;
    private final Long orderId;

    public OrderItemEntity(
            final String name,
            final String imageUrl,
            final long price,
            final Integer quantity,
            final Long orderId
    ) {
        this(null, name, imageUrl, price, quantity, orderId);
    }

    public OrderItemEntity(
            final Long id,
            final String name,
            final String imageUrl,
            final long price,
            final Integer quantity,
            final Long orderId
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public static OrderItemEntity from(final CartItem cartItem, final Long orderId) {
        final Product product = cartItem.getProduct();
        return new OrderItemEntity(
                product.getName(),
                product.getImageUrl(),
                product.getPrice().getLongValue(),
                cartItem.getQuantity(),
                orderId
        );
    }

    public CartItem toDomain(final Long memberId) {
        return new CartItem(id, quantity, memberId, new Product(name, imageUrl, price));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return orderId;
    }
}
