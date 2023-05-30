package cart.domain;

import cart.domain.value.DiscountRate;
import cart.domain.value.ImageUrl;
import cart.domain.value.Name;
import cart.domain.value.Price;

public class Product {
    private final Long id;
    private final Name name;
    private final Price price;
    private final ImageUrl imageUrl;
    private final boolean isDiscounted;
    private final DiscountRate discountRate;

    public Product(
            final Long id,
            final Name name,
            final Price price,
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
                new Price(price),
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
                new Price(price),
                new ImageUrl(imageUrl),
                isDiscounted,
                new DiscountRate(discountRate)
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public int getPrice() {
        return price.getPrice();
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
