package cart.domain;

public class Product {
    private final Long id;
    private final Name name;
    private final Money price;
    private final String imageUrl;

    public Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Money(price);
        this.imageUrl = imageUrl;
    }

    public Product(final String name, final int price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public String getNameValue() {
        return name.getName();
    }

    public int getPriceValue() {
        return price.getMoneyValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
