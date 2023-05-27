package cart.dto;

import cart.domain.Product1;

public class ProductResponse1 {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isOnSale;
    private int discountPrice;

    public ProductResponse1(Long id, String name, int price, String imageUrl, boolean isOnSale, int discountPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.discountPrice = discountPrice;
    }

    public static ProductResponse1 of(Product1 product) {
        return new ProductResponse1(
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

    public boolean isOnSale() {
        return isOnSale;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
