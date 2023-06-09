package cart.entity;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final boolean isDiscounted;
    private final int discountRate;

    public ProductEntity(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final boolean isDiscounted,
            final int discountRate
    ) {
        this.id = id;
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

    public boolean getIsDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
