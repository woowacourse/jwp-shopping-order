package cart.domain.order;

public class ProductHistory {

    private final Long id;
    private final String productName;
    private final int price;

    public ProductHistory(final Long id, final String productName, final int price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }
}
