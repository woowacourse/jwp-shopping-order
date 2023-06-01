package cart.application.domain;

public class OrderInfo {

    private final Long id;
    private final Product product;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer quantity;

    public OrderInfo(Long id, Product product, String name, Integer price, String imageUrl, Integer quantity) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
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
