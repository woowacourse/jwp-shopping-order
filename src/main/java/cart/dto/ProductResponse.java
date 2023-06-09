package cart.dto;

import cart.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isOnSale;
    private int discountPrice;

    public ProductResponse(Long id, String name, int price, String imageUrl, boolean isOnSale, int discountPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.discountPrice = discountPrice;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice().getValue(),
                product.getImageUrl(),
                product.isOnSale(),
                product.getDiscountPrice().getValue()
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

    public boolean getIsOnSale() {
        return isOnSale;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
