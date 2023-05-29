package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderResponse {
    @JsonProperty("priceBeforeDiscount")
    private final int totalPrice;
    @JsonProperty("priceAfterDiscount")
    private final int finalPrice;
    private final List<ProductInOrderResponse> products;

    public OrderResponse(
            final int totalPrice,
            final int finalPrice,
            final List<ProductInOrderResponse> products
    ) {
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.products = products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public List<ProductInOrderResponse> getProducts() {
        return products;
    }
}
