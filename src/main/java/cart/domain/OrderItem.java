package cart.domain;

public class OrderItem {
    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Integer quantity;

    public OrderItem(final String name, final Long price, final String imageUrl, final Integer quantity) {
        this(null, name, price, imageUrl, quantity);
    }

    public OrderItem(final Long id, final String name, final Long price, final String imageUrl,
                     final Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public long getCalculatePrice() {
        return price * quantity;
    }
}
