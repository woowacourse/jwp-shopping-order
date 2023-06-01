package cart.dto;

import cart.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int discountRate;
    private int discountedPrice;

    private ProductResponse(Long id, String name, int price, String imageUrl, int discountRate, int discountedPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getDiscountRate(), product.getDiscountedPrice());
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

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }
}
