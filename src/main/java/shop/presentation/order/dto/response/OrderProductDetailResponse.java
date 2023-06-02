package shop.presentation.order.dto.response;

public class OrderProductDetailResponse {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    private OrderProductDetailResponse() {
    }

    public OrderProductDetailResponse(Long id, String name, Integer price, String imageUrl) {
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
