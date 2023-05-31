package cart.dto;

import cart.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Double pointRatio;
    private final boolean pointAvailable;

    private ProductResponse(Long id, String name, int price, String imageUrl, Double pointRatio, boolean pointAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getPointRatio(), product.getPointAvailable());
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

    public Double getPointRatio() {
        return pointRatio;
    }

    public boolean getPointAvailable() {
        return pointAvailable;
    }
}
