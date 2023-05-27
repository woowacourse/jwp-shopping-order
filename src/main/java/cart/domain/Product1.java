package cart.domain;

public class Product1 {
    private Long id;
    private String name;
    private Price price;
    private String imageUrl;
    private Percentage salePercentage;

    public Product1(String name, Price price, String imageUrl) {
        this(null, name, price, imageUrl, null);
    }

    public Product1(String name, Price price, String imageUrl, Percentage salePercentage) {
        this(null, name, price, imageUrl, salePercentage);
    }

    public Product1(Long id, String name, Price price, String imageUrl, Percentage salePercentage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.salePercentage = salePercentage;
    }

    public Price getSalePrice() {
        if (isOnSale()) {
            return price.discount(salePercentage);
        }
        return price;
    }

    public boolean isOnSale() {
        return salePercentage != null;
    }

    public Price getDiscountPrice() {
        return price.minus(getSalePrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
