package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.value.*;

public class OrderItem {

    private final Long id;
    private final Name name;
    private final Money price;
    private final ImageUrl imageUrl;
    private final Quantity quantity;
    private final DiscountRate discountRate;

    public OrderItem(
            final Long id,
            final Name name,
            final Money price,
            final ImageUrl imageUrl,
            final Quantity quantity,
            final DiscountRate discountRate
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
    }

    public OrderItem(
            final String name,
            final int price,
            final String imageUrl,
            final int quantity,
            final int discountRate
    ) {
        this(
                null,
                new Name(name),
                new Money(price),
                new ImageUrl(imageUrl),
                new Quantity(quantity),
                new DiscountRate(discountRate)
        );
    }

    public OrderItem(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final int quantity,
            final int discountRate
    ) {
        this(
                id,
                new Name(name),
                new Money(price),
                new ImageUrl(imageUrl),
                new Quantity(quantity),
                new DiscountRate(discountRate)
        );
    }

    public int getItemDiscountedPrice() {
        double discountedPercent = discountRate.getDiscountedPercent();
        return price.multiply(discountedPercent) * getQuantity();
    }

    public int getMemberDiscountedPrice(final Member member) {
        return member.discountPrice(getPrice()) * getQuantity();
    }

    public int getPrinciplePrice() {
        return getPrice() * getQuantity();
    }

    public boolean isDiscount() {
        return discountRate.isDiscount();
    }

    public boolean isNotDiscount() {
        return !discountRate.isDiscount();
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

    public int getQuantity() {
        return quantity.getQuantity();
    }

    public int getDiscountRate() {
        return discountRate.getDiscountRate();
    }
}
