package cart.entity;

import cart.domain.CartItem;

public class OrderProductEntity {
    private final Long id;
    private final String name;
    private final String image_url;
    private final Integer price;
    private final Integer quantity;
    private final Long orderId;

    public OrderProductEntity(String name, String image_url, Integer price, Integer quantity, Long orderId) {
        this(null, name, image_url, price, quantity, orderId);
    }

    public OrderProductEntity(Long id, String name, String image_url, Integer price, Integer quantity, Long orderId) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public static OrderProductEntity of(CartItem cartItem, Long orderId) {
        return new OrderProductEntity(cartItem.getProduct().getName(),
                cartItem.getProduct().getImageUrl(), cartItem.getProduct().getPrice(),
                cartItem.getQuantity(), orderId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return orderId;
    }
}
