package cart.dto.product;

public class ProductRequest {
    private String name;
    private int price;
    private String imageUrl;
    private boolean isDiscounted;
    private int discountRate;

    public ProductRequest() {
    }

    public ProductRequest(final String name, final int price, final String imageUrl, final boolean isDiscounted, final int discountRate) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
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
