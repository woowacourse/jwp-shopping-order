package cart.dto.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderRequest {

    @Size(min = 1)
    private List<Long> cartItemIds;

    @NotNull
    private BigDecimal usePoint;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItemIds, final BigDecimal usePoint) {
        this.cartItemIds = cartItemIds;
        this.usePoint = usePoint;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public BigDecimal getUsePoint() {
        return usePoint;
    }
}
