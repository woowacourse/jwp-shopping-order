package cart.domain;

public class OrderItem {

    private final Product product;
    private final int quantity;
    private final int totalPrice;

    public OrderItem(Product product, int quantity, int totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
