package cart.domain;

public class OrderProduct {

    private final Product product;
    private final Integer purchasedPrice;
    private final Integer quantity;

    public OrderProduct(final Product product, final Integer purchasedPrice, final Integer quantity) {
        this.product = product;
        this.purchasedPrice = purchasedPrice;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getPurchasedPrice() {
        return purchasedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
