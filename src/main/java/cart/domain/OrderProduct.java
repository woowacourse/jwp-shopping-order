package cart.domain;

public class OrderProduct {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer quantity;

    public OrderProduct(Long id, String name, Integer price, String imageUrl, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public OrderProduct(String name, Integer price, String imageUrl, Integer quantity) {
        this(null, name, price, imageUrl, quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
