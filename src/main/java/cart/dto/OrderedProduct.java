package cart.dto;

public class OrderedProduct {
    private String name;
    private Long price;
    private Long quantity;
    private String imageUrl;

    public OrderedProduct() {
    }

    public OrderedProduct(final String name, final Long price, final Long quantity, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
