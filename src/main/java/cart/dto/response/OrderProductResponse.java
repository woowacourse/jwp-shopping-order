package cart.dto.response;

public class OrderProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public OrderProductResponse() {
    }

    public OrderProductResponse(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
