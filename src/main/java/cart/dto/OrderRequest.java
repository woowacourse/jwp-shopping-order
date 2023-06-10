package cart.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotEmpty
    private final List<Long> cartItemIds;
    @Positive
    private final Long originalPrice;
    @PositiveOrZero
    private final Long usedPoint;
    @PositiveOrZero
    private final Long pointToAdd;

    public OrderRequest(List<Long> cartItemIds, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.cartItemIds = cartItemIds;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getPointToAdd() {
        return pointToAdd;
    }
}
