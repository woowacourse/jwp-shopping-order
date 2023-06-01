package cart.domain;

import cart.domain.value.DiscountRate;
import cart.domain.value.ImageUrl;
import cart.domain.value.Money;
import cart.domain.value.Name;

public class Product {
    private final Long id;
    private final Name name;
    private final Money price;
    private final ImageUrl imageUrl;
    private final boolean isDiscounted;
    private final DiscountRate discountRate;

    public Product(
            final Long id,
            final Name name,
            final Money price,
            final ImageUrl imageUrl,
            final boolean isDiscounted,
            final DiscountRate discountRate
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
    }

    public Product(final String name,
                   final int price,
                   final String imageUrl,
                   final boolean isDiscounted,
                   final int discountRate
    ) {
        this(
                null,
                new Name(name),
                new Money(price),
                new ImageUrl(imageUrl),
                isDiscounted,
                new DiscountRate(discountRate)
        );
    }

    public Product(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final boolean isDiscounted,
            final int discountRate
    ) {
        this(
                id,
                new Name(name),
                new Money(price),
                new ImageUrl(imageUrl),
                isDiscounted,
                new DiscountRate(discountRate)
        );
    }

    public int getDiscountedPrice() {
        return (int) (getPrice() * (1 - (double) getDiscountRate()/100));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public int getPrice() {
        return price.getMoney();
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public int getDiscountRate() {
        return discountRate.getDiscountRate();
    }
}
