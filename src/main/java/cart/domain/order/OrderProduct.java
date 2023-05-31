package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;

public class OrderProduct {

    private Long id;
    private Product product;
    private Quantity quantity;

    private OrderProduct() {
    }

    public OrderProduct(Product product, int quantity) {
        this(null, product, quantity);
    }

    public OrderProduct(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public static OrderProduct from(CartItem cartItem) {
        return new OrderProduct(cartItem.getProduct(), cartItem.getQuantity());
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public int calculateTotalPrice() {
        return product.getPrice() * quantity.getValue();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public int getQuantity() {
        return quantity.getValue();
    }
}
