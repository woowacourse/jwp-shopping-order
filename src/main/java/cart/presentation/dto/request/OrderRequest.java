package cart.presentation.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotEmpty
    private List<Long> cartItemIds;
    @Positive
    @NotNull
    private Long originalPrice;
    @PositiveOrZero
    private Long usedPoint;
    @PositiveOrZero
    private Long pointToAdd;

    public OrderRequest() {
    }

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
