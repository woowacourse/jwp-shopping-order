package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrderRequest {
    private List<Long> cartItemIds;
    private Long couponId;

    public OrderRequest() {

    }

    public OrderRequest(final List<Long> cartItemIds, final Long couponId) {
        this.cartItemIds = cartItemIds;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Long getCouponId() {
        return couponId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderRequest that = (OrderRequest) o;
        return Objects.equals(cartItemIds, that.cartItemIds)
                && Objects.equals(couponId, that.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemIds, couponId);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "cartItemsIds=" + cartItemIds +
                ", couponId=" + couponId +
                '}';
    }
}
