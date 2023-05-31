package cart.dao.entity;

import cart.domain.CartItem;
import cart.domain.Product;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderItemEntity {

    private final long orderId;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final int quantity;
    private Long id;

    public OrderItemEntity(final long id,
                           final long orderId,
                           final String name,
                           final long price,
                           final String imageUrl,
                           final int quantity) {
        this(orderId, name, price, imageUrl, quantity);
        this.id = id;
    }

    public OrderItemEntity(final long orderId,
                           final String name,
                           final long price,
                           final String imageUrl,
                           final int quantity) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemEntity of(final long orderId, final CartItem cartItem) {
        final Product product = cartItem.getProduct();
        return new OrderItemEntity(orderId,
                product.getName(),
                product.getPrice().getValue(),
                product.getImageUrl(),
                cartItem.getQuantity());
    }

    public static List<OrderItemEntity> of(final long orderId, final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> OrderItemEntity.of(orderId, cartItem))
                .collect(Collectors.toList());
    }

    public long getOrderId() {
        return orderId;
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

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderItemEntity that = (OrderItemEntity) o;
        return orderId == that.orderId && price == that.price && quantity == that.quantity && Objects.equals(name,
                that.name) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, name, price, imageUrl, quantity, id);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "orderId=" + orderId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                ", id=" + id +
                '}';
    }
}
