package cart.product.presentation.dto;

import cart.product.application.dto.UpdateProductCommand;

public class UpdateProductRequest {

    private String name;
    private int price;
    private String imageUrl;

    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public UpdateProductCommand toCommand() {
        return new UpdateProductCommand(name, price, imageUrl);
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
