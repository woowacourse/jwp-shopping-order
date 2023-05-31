package cart.domain;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private Double pointRatio;
    private boolean pointAvailable;

    public Product(String name, int price, String imageUrl, Double pointRatio, boolean pointAvailable) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.pointRatio = pointRatio;
        this.pointAvailable = pointAvailable;
    }

    public Product(Long id, String name, int price, String imageUrl, Double pointRatio, boolean pointAvailable) {
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
