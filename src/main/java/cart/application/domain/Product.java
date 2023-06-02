package cart.application.domain;

public class Product {

    private final Long id;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final double pointRatio;
    private final boolean pointAvailable;

    public Product(Long id, String name, long price, String imageUrl, double pointRatio, boolean pointAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPointRatio() {
        return pointRatio;
    }

    public boolean isPointAvailable() {
        return pointAvailable;
    }
}
