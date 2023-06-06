package cart.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.URL;

public class CartItemRequest {
    @NotNull
    @Positive
    private Long cartItemId;
    @NotNull
    @Positive
    private int quantity;
    @NotEmpty
    private String name;
    @NotNull
    @PositiveOrZero
    private int price;
    @NotEmpty
    @URL
    private String imageUrl;

    public CartItemRequest() {
    }

    public CartItemRequest(Long cartItemId, int quantity, String name, int price, String imageUrl) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
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
