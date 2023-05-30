package cart.domain.product;

import cart.domain.common.Money;

public class Product {
    private final Long id;
    private final Name name;
    private final Money price;
    private final ImageUrl imageUrl;

    public Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = new Name(name);
        this.price = Money.valueOf(price);
        this.imageUrl = new ImageUrl(imageUrl);
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

    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public String getNameValue() {
        return name.getName();
    }

    public int getPriceValue() {
        return price.getMoneyValue();
    }

    public String getImageUrlValue() {
        return imageUrl.getImageUrl();
    }
}
