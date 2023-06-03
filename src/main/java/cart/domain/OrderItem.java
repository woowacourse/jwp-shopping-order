package cart.domain;

import cart.domain.value.ImageUrl;
import cart.domain.value.Money;
import cart.domain.value.Name;
import cart.domain.value.Quantity;
import cart.domain.value.Rate;

public class OrderItem {

    private final Long id;
    private final Name name;
    private final Money price;
    private final ImageUrl imageUrl;
    private final Quantity quantity;
    private final Rate rate;

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
        this.rate = new Rate(discountRate);
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
        this.rate = new Rate(discountRate);
    }

    public boolean isMemberDiscount() {
        return rate.getValue() == 0;
    }

    public boolean isProductDiscount() {
        return rate.getValue() != 0;
    }

    public int calculateDiscountAmount() {
        return (int) (price.getValue() * (rate.getValue() / 100.0) * quantity.getValue());
    }

    public int calculateMemberDiscountAmount(final double discountedRateByGrade) {
        return (int) (price.getValue() * discountedRateByGrade * quantity.getValue());
    }

    public int calculateDiscountedPriceBy(final double discountRate) {
        return (int) (price.getValue() * (1 - (discountRate / 100.0)) * quantity.getValue());
    }

    public int calculateDiscountedPrice() {
        return (int) (price.getValue() * (1 - (rate.getValue() / 100.0)) * quantity.getValue());
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
        return rate.getValue();
    }
}
