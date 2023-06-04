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
    private final Rate discountRate;

    public OrderItem(final Product product, final CartItem cartItem) {
        this.id = null;
        this.name = new Name(product.getName());
        this.price = new Money(product.getPrice());
        this.imageUrl = new ImageUrl(product.getImageUrl());
        this.quantity = new Quantity(cartItem.getQuantity());
        this.discountRate = new Rate(product.getDiscountRate());
    }


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
        this.discountRate = new Rate(discountRate);
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
        this.discountRate = new Rate(discountRate);
    }

    public boolean isMemberDiscount() {
        return discountRate.getValue() == 0;
    }

    public boolean isProductDiscount() {
        return discountRate.getValue() != 0;
    }

    public int calculateDiscountAmount() {
        return (int) (price.getValue() * (discountRate.getValue() / 100.0) * quantity.getValue());
    }

    public int calculateMemberDiscountAmount(final double discountedRateByGrade) {
        return (int) (price.getValue() * discountedRateByGrade * quantity.getValue());
    }

    public int calculateDiscountedPriceByGrade(final double discountedRateByGrade) {
        return (int) (price.getValue() * (1 - discountedRateByGrade) * quantity.getValue());
    }

    public int calculateDiscountedPrice() {
        return (int) (price.getValue() * (1 - (discountRate.getValue() / 100.0)) * quantity.getValue());
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
