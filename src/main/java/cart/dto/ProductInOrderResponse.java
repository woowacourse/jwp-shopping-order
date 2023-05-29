package cart.dto;

public class ProductInOrderResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final int count;
    private final String imageUrl;

    public ProductInOrderResponse(
            final Long id,
            final String name,
            final int price,
            final int count,
            final String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
