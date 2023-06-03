package cart.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private final int usedPoints;
    private final List<CartItemRequest> cartItems;

    public OrderCreateRequest(final int usedPoints, final List<CartItemRequest> cartItems) {
        this.usedPoints = usedPoints;
        this.cartItems = cartItems;
    }

    public List<Long> toCartItemIds() {
        return cartItems.stream()
                .map(CartItemRequest::getId)
                .collect(Collectors.toList());
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public List<CartItemRequest> getCartItems() {
        return new ArrayList<>(cartItems);
    }
}
