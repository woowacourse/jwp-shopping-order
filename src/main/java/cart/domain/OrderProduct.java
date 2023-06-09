package cart.domain;

public class OrderProduct {

    private Long productId;
    private final String name;
    private final Money price;
    private final String imageUrl;

    public OrderProduct(String name, Money price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public OrderProduct(Long productId, String name, Money price, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static OrderProduct fromProduct(Product product) {
        return new OrderProduct(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl());
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getPriceIntValue() {
        return price.getIntValue();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
