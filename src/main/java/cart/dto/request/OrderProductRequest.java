package cart.dto.request;

import cart.domain.Product;

public class OrderProductRequest {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public OrderProductRequest() {
    }

    public OrderProductRequest(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toDomain() {
        return new Product(id, name, price, imageUrl);
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
}
