package cart.entity;

import cart.domain.Product;
import cart.domain.ProductStock;
import cart.domain.Stock;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int stock;

    public ProductEntity(final Long id, final ProductEntity other) {
        this(id, other.name, other.price, other.imageUrl, other.stock);
    }

    public ProductEntity(final String name, final int price, final String imageUrl, final int stock) {
        this(null, name, price, imageUrl, stock);
    }

    public ProductEntity(final Long id, final String name, final int price, final String imageUrl, final int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public ProductStock toProductStock() {
        return new ProductStock(toProduct(), new Stock(stock));
    }

    public Product toProduct() {
        return new Product(id, name, price, imageUrl);
    }

    public Stock toStock() {
        return new Stock(stock);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }
}
