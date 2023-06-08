package cart.product.presentation.dto;

import cart.product.application.dto.CreateProductCommand;

public class CreateProductRequest {

    private String name;
    private int price;
    private String imageUrl;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public CreateProductCommand toCommand() {
        return new CreateProductCommand(name, price, imageUrl);
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
