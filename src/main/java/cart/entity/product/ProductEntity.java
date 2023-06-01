package cart.entity.product;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Boolean isOnSale;
    private final int salePrice;

    public ProductEntity(final Long id, final String name, final int price, final String imageUrl, final Boolean isOnSale, final int salePrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.salePrice = salePrice;
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

    public boolean isOnSale() {
        return isOnSale;
    }

    public int getSalePrice() {
        return salePrice;
    }
}