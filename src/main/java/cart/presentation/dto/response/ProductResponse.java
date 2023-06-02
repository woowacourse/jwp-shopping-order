package cart.presentation.dto.response;

import cart.application.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Double pointRatio;
    private final Boolean pointAvailable;

    private ProductResponse(Long id, String name, Long price, String imageUrl,
                            double pointRatio, boolean pointAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(), product.getPointRatio(), product.isPointAvailable());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getPointRatio() {
        return pointRatio;
    }

    public Boolean getPointAvailable() {
        return pointAvailable;
    }
}
