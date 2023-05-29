package cart.dto;

import cart.domain.Product;
import cart.domain.ProductStock;
import cart.domain.Stock;

public class ProductStockResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int stock;

    private ProductStockResponse(final Long id, final String name, final int price, final String imageUrl, final int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public static ProductStockResponse of(final ProductStock productStock) {
        final Product product = productStock.getProduct();
        final Stock stock = productStock.getStock();
        return new ProductStockResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), stock.getValue());
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
