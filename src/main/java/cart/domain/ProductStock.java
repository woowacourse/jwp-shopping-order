package cart.domain;

public class ProductStock {

    private final Product product;
    private final Stock stock;

    public ProductStock(final Product product, final Stock stock) {
        this.product = product;
        this.stock = stock;
    }

    public Product getProduct() {
        return product;
    }

    public Stock getStock() {
        return stock;
    }
}
