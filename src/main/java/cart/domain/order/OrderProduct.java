package cart.domain.order;

import cart.domain.product.Product;

public class OrderProduct {

    private Long id;
    private Product product;
    private int quantity;

    private OrderProduct() {
    }

    public OrderProduct(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
