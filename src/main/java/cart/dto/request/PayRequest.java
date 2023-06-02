package cart.dto.request;

import java.util.List;

public class PayRequest {
    private final List<Long> cartItemIds;
    private final int originalPrice;
    private final int points;

    public PayRequest(final List<Long> cartItemIds, final int originalPrice, final Integer points) {
        this.cartItemIds = cartItemIds;
        this.originalPrice = originalPrice;
        this.points = points;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public Integer getPoints() {
        return points;
    }
}
