package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrderRequest {
    private List<Long> cartItemsIds;
    private Long couponId;

    public OrderRequest() {

    }

    public OrderRequest(final List<Long> cartItemsIds, final Long couponId) {
        this.cartItemsIds = cartItemsIds;
        this.couponId = couponId;
    }

    public List<Long> getCartItemsIds() {
        return cartItemsIds;
    }

    public Long getCouponId() {
        return couponId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderRequest that = (OrderRequest) o;
        return Objects.equals(cartItemsIds, that.cartItemsIds)
                && Objects.equals(couponId, that.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemsIds, couponId);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "cartItemsIds=" + cartItemsIds +
                ", couponId=" + couponId +
                '}';
    }
}
