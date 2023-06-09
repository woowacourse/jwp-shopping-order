package cart.order.application.dto;

import cart.product.domain.Product;

public class ProductInCartItemDto {
    private Long productId;
    private int price;
    private String name;
    private String imageUrl;
    private int stock;

    public ProductInCartItemDto(Long productId, int price, String name, String imageUrl, int stock) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public static ProductInCartItemDto from(Product product) {
        return new ProductInCartItemDto(product.getId(), product.getPrice(), product.getName(), product.getImageUrl(),
                product.getStock());
    }

    public Product toDomain() {
        return new Product(productId, name, price, imageUrl, stock);
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }
}
