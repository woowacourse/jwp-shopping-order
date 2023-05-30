package cart.domain;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isDiscounted;
    private int discountRate;

    public Product(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, int price, String imageUrl, boolean isDiscounted, int discountRate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
    }

    public Product(String name, int price, String imageUrl, boolean isDiscounted, int discountRate) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
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

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int calculateDiscountedPrice(int discountedPercentage) {
        if (isDiscounted()) {
            return (discountRate / 100 * price - price) * -1;
        } else {
            return (discountedPercentage / 100 * price - price) * -1;
        }
    }
}
