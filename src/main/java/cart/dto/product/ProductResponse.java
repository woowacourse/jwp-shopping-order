package cart.dto.product;

import cart.domain.product.Product;

public class ProductResponse {

    private long id;
    private String name;
    private int price;
    private String imageUrl;
    private Boolean isOnSale;
    private int salePrice;

    public ProductResponse() {
    }

    public ProductResponse(final long id, final String name, final int price, final String imageUrl, final Boolean isOnSale, final int salePrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.salePrice = salePrice;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.isOnSale(), product.getSalePrice());
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

    public Boolean getIsOnSale() {
        return isOnSale;
    }

    public int getSalePrice() {
        return salePrice;
    }
}
