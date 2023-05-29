package cart.domain.order;

public class ProductHistory {

    private final Long id;
    private final String productName;
    private final int quantity;
    private int price;

    public ProductHistory(final Long id, final String productName, final int quantity, final int price) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }
}
