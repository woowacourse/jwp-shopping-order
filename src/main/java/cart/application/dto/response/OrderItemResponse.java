package cart.application.dto.response;

public class OrderItemResponse {
    private final String name;
    private final String imageUrl;
    private final Integer count;
    private final Integer price;

    public OrderItemResponse(final String name, final String imageUrl, final Integer count, final Integer price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPrice() {
        return price;
    }
}
