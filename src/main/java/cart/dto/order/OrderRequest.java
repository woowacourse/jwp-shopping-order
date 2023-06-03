package cart.dto.order;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Objects;

public class OrderRequest {

    @NotNull
    private List<Long> cartItemIds;
    @PositiveOrZero
    private int totalPrice;
    @Nullable
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, int totalPrice, Long couponId) {
        this.cartItemIds = cartItemIds;
        this.totalPrice = totalPrice;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Long getCouponId() {
        return couponId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderRequest that = (OrderRequest) o;
        return totalPrice == that.totalPrice && Objects.equals(cartItemIds, that.cartItemIds) && Objects.equals(couponId, that.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemIds, totalPrice, couponId);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "cartItemIds=" + cartItemIds +
                ", totalPrice=" + totalPrice +
                ", couponId=" + couponId +
                '}';
    }
}
