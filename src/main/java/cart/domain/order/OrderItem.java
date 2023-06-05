package cart.domain.order;

import cart.domain.VO.Money;

public class OrderItem {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Money price;
    private final Integer quantity;

    public OrderItem(final String name, final String imageUrl, final Money price, final Integer quantity) {
        this(null, name, imageUrl, price, quantity);
    }

    public OrderItem(
            final Long id,
            final String name,
            final String imageUrl,
            final Money price,
            final Integer quantity
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public Money calculateTotalPrice() {
        return price.times(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Money getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
