package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItem {

    private Long id;
    private Long orderId;
    private String name;
    private Money price;
    private String imageUrl;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(final Long orderId,
                     final String name,
                     final Money price,
                     final String imageUrl,
                     final Integer quantity) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public OrderItem(final Long id,
                     final Long orderId,
                     final String name,
                     final Money price,
                     final String imageUrl,
                     final Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static List<OrderItem> from(final Long orderId, final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(
                        orderId,
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getImageUrl(),
                        cartItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price.getValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
