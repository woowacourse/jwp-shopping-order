package cart.dto.product;

import cart.domain.product.Product;

public class ProductResponse {

    private long id;
    private String name;
    private int price;
    private String imageUrl;
    private Integer salePrice;

    public ProductResponse() {
    }

    private ProductResponse(final long id, final String name, final int price, final String imageUrl, final Integer salePrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.salePrice = salePrice;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getSalePrice());
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

    public Integer getSalePrice() {
        return salePrice;
    }
}
