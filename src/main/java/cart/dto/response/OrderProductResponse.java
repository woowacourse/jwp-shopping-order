package cart.dto.response;

public class OrderProductResponse {

    private final String name;
    private final String imageUrl;
    private final int count;
    private final int price;

    public OrderProductResponse(
            final String name,
            final String imageUrl,
            final int count,
            final int price
    ) {
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

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
