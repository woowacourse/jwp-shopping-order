package cart.domain.order.domain.dto;

public class OrderItemDto {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    private OrderItemDto() {
    }

    public OrderItemDto(Long id, String name, int price, String imageUrl) {
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

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
