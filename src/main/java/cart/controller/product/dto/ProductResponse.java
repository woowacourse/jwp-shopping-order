package cart.controller.product.dto;

import cart.product.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isOnSale;
    private int salePrice;

    private ProductResponse(Long id, String name, int price, String imageUrl, boolean isOnSale, int salePrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.salePrice = salePrice;
    }

    public static ProductResponse from(Long id, String name, int price, String imageUrl, boolean isOnSale, int salePrice) {
        return new ProductResponse(id, name, price, imageUrl, isOnSale, salePrice);
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getDiscountPrice() != 0,
                product.getDiscountPrice()
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

    @JsonProperty("isOnSale")
    public boolean isOnSale() {
        return isOnSale;
    }

    public int getSalePrice() {
        return salePrice;
    }
}
