package cart.dto;

public class ProductRequest {
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Double pointRatio;
    private final boolean pointAvailable;

    public ProductRequest(String name, int price, String imageUrl, Double pointRatio, boolean pointAvailable) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
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
