package cart.domain;

import cart.domain.value.DiscountRate;
import cart.domain.value.ImageUrl;
import cart.domain.value.Money;
import cart.domain.value.Name;
import cart.domain.value.Quantity;

public class OrderItem {

    private final Long id;
    private final Name name;
    private final Money price;
    private final ImageUrl imageUrl;
    private final Quantity quantity;
    private final DiscountRate discountRate;

    public OrderItem(
            final String name,
            final int price,
            final String imageUrl,
            final int quantity,
            final int discountRate
    ) {
        this.id = null;
        this.name = new Name(name);
        this.price = new Money(price);
        this.imageUrl = new ImageUrl(imageUrl);
        this.quantity = new Quantity(quantity);
        this.discountRate = new DiscountRate(discountRate);
    }

    public OrderItem(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final int quantity,
            final int discountRate
    ) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Money(price);
        this.imageUrl = new ImageUrl(imageUrl);
        this.quantity = new Quantity(quantity);
        this.discountRate = new DiscountRate(discountRate);
    }

    public boolean isMemberDiscount() {
        return discountRate.getValue() == 0;
    }

    public boolean isProductDiscount() {
        return discountRate.getValue() != 0;
    }

    public int calculateDiscountedPriceBy(final double discountRate) {
        return (int) (price.getValue() * (1 - (discountRate / 100.0)));
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

    public int getQuantity() {
        return quantity.getValue();
    }

    public int getDiscountRate() {
        return discountRate.getValue();
    }
}
