package cart.domain;

public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderItem of(Product product, int quantity) {
        product.sold(quantity);
        return new OrderItem(product, quantity);
    }

    public int calculatePrice() {
        return product.getPrice() * quantity;
    }
}
