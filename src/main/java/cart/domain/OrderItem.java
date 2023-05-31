package cart.domain;

import cart.dao.entity.OrderItemEntity;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderItem {

    private final String name;
    private final Money price;
    private final String imageUrl;
    private final Integer quantity;
    private Long id;

    private OrderItem(final String name,
                      final Money price,
                      final String imageUrl,
                      final Integer quantity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    private OrderItem(final long id,
                      final String name,
                      final Money price,
                      final String imageUrl,
                      final Integer quantity) {
        this(name, price, imageUrl, quantity);
        this.id = id;
    }

    public static OrderItem convert(final CartItem cartItem) {
        final Product product = cartItem.getProduct();
        return new OrderItem(product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                cartItem.getQuantity());
    }

    public static List<OrderItem> convert(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(OrderItem::convert)
                .collect(Collectors.toList());
    }

    public static OrderItem from(final OrderItemEntity orderItemEntity) {
        return new OrderItem(
                orderItemEntity.getId(),
                orderItemEntity.getName(),
                new Money(orderItemEntity.getPrice()),
                orderItemEntity.getImageUrl(),
                orderItemEntity.getQuantity());
    }

    public static List<OrderItem> from(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(OrderItem::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItem orderItem = (OrderItem) o;
        return Objects.equals(name, orderItem.name) && Objects.equals(price, orderItem.price)
                && Objects.equals(imageUrl, orderItem.imageUrl) && Objects.equals(quantity,
                orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, imageUrl, quantity);
    }
}
