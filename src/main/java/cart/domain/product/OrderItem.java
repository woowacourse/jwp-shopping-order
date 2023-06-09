package cart.domain.product;

public class OrderItem extends Item {

    public OrderItem(final Product product, int quantity) {
        super(product, quantity);
    }

    public OrderItem(final Long id, final int quantity, final Product product) {
        super(id, quantity, product);
    }

    public static OrderItem of(final CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.quantity);
    }
}
