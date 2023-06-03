package cart.domain;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isDiscounted;
    private int discountRate;
    private int discountedPrice;

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
        this.discountedPrice = calculateDiscountedPrice();
    }

    public Product(String name, int price, String imageUrl, boolean isDiscounted, int discountRate) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
        this.discountedPrice = calculateDiscountedPrice();
    }

    public Product(Long id, String name, int price, String imageUrl, int discountRate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discountRate = discountRate;
    }

    public int calculateDiscountedPrice(int memberDiscount) {
        if (getIsDiscounted()) {
            return (discountRate * price / 100 - price) * -1;
        } else {
            return (memberDiscount * price / 100 - price) * -1;
        }
    }


    public int calculateMemberDiscountedPrice(int memberDiscount) {
        if (!getIsDiscounted() && memberDiscount > 0) {
            return price - ((memberDiscount * price / 100 - price) * -1);
        }
        return 0;
    }

    public int calculateDiscountedPrice() {
        if (getIsDiscounted()) {
            return price - ((discountRate * price / 100 - price) * -1);
        }
        return 0;
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

    public boolean getIsDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }
}
