package cart.dto;

import cart.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int discountRate;
    private int discountedPrice;

    public ProductResponse(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final int discountRate,
            final int discountedPrice
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getDiscountRate(),
                product.calculateDiscountedPrice()
        );
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
