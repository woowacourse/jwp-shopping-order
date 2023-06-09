package cart.dto;

public class ProductRequest {

    private String name;
    private int price;
    private String imageUrl;
    private boolean isDiscounted;
    private int discountRate;

    public ProductRequest() {
    }

    public ProductRequest(String name, int price, String imageUrl, int discountRate) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discountRate = discountRate;
        if (discountRate > 0) {
            this.isDiscounted = true;
        }
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

    public boolean getIsDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
