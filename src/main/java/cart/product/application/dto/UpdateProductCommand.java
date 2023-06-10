package cart.product.application.dto;

public class UpdateProductCommand {

    private final String name;
    private final int price;
    private final String imageUrl;

    public UpdateProductCommand(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
