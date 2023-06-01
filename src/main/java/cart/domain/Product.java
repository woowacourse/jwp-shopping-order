package cart.domain;

import cart.domain.value.DiscountRate;
import cart.domain.value.ImageUrl;
import cart.domain.value.Money;
import cart.domain.value.Name;

public class Product {
    private Long id;
    private Name name;
    private Money price;
    private ImageUrl imageUrl;
    private boolean isDiscounted;
    private DiscountRate discountRate;

    public Product(
            final String name,
            final int price,
            final String imageUrl
    ) {
        this.name = new Name(name);
        this.price = new Money(price);
        this.imageUrl = new ImageUrl(imageUrl);
    }

    public Product(
            final Long id,
            final String name,
            final int price,
            final String imageUrl
    ) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Money(price);
        this.imageUrl = new ImageUrl(imageUrl);
    }

    public Product(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final boolean isDiscounted,
            final int discountRate
    ) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Money(price);
        this.imageUrl = new ImageUrl(imageUrl);
        this.isDiscounted = isDiscounted;
        this.discountRate = new DiscountRate(discountRate);
    }

    public int calculateDiscountedPrice() {
        return (int) (price.getValue() * (1 - (discountRate.getValue() / 100.0)));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public int getPrice() {
        return price.getValue();
    }

    public String getImageUrl() {
        return imageUrl.getValue();
    }

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate.getValue();
    }
}
