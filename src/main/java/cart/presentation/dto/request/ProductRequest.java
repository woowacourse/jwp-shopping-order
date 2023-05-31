package cart.presentation.dto.request;

public class ProductRequest {

    private String name;
    private int price;
    private String imageUrl;
    private Double pointRatio;
    private Boolean pointAvailable;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price, String imageUrl, Double pointRatio, Boolean pointAvailable) {
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

    public Boolean getPointAvailable() {
        return pointAvailable;
    }
}
