package cart.domain;

public class Product {
    private final Long id;
    private final String name;
    private final Money price;
    private final String imageUrl;

    public Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = new Money(price);
        this.imageUrl = imageUrl;
    }

    public Product(final String name, final int price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getPriceValue() {
        return price.getMoneyValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
