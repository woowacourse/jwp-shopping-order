package cart.domain;

import cart.domain.value.DiscountRate;
import cart.domain.value.ImageUrl;
import cart.domain.value.Name;
import cart.domain.value.Price;
import cart.domain.value.Quantity;

public class OrderItem {

    private final Long id;
    private final Name name;
    private final Price price;
    private final ImageUrl imageUrl;
    private final Quantity quantity;
    private final DiscountRate discountRate;

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
        this.price = new Price(price);
        this.imageUrl = new ImageUrl(imageUrl);
        this.quantity = new Quantity(quantity);
        this.discountRate = new DiscountRate(discountRate);
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
