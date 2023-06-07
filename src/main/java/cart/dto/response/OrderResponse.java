package cart.dto.response;

import java.util.List;

public class OrderResponse {
    private final Long id;
    private final int savingRate;
    private final int points;
    private final List<OrderItemResponse> cartItems;

    public OrderResponse(final Long id, final int savingRate, final int points, final List<OrderItemResponse> cartItems) {
        this.id = id;
        this.savingRate = savingRate;
        this.points = points;
        this.cartItems = cartItems;
    }

    public Long getId() {
        return id;
    }

    public int getSavingRate() {
        return savingRate;
    }

    public int getPoints() {
        return points;
    }

    public List<OrderItemResponse> getCartItems() {
        return cartItems;
    }
}
