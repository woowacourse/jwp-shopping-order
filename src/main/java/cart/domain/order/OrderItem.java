package cart.domain.order;

import cart.domain.cart.CartItem;
import cart.domain.Product;
import cart.entity.OrderItemEntity;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final Product product;

    private OrderItem(final int quantity, final Product product) {
        this(null, quantity, product);
    }

    private OrderItem(final Long id, final int quantity, final Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderItem from(CartItem cartItem) {
        return new OrderItem(cartItem.getQuantity(), cartItem.getProduct());
    }

    public static OrderItem of(OrderItemEntity orderItemEntity, Product product) {
        return new OrderItem(orderItemEntity.getId(), orderItemEntity.getQuantity(), product);
    }

    public int getPrice() {
        return quantity * product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

}
