package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrderRequest {

    private List<Long> cartItemIds;
    private int totalPrice;
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
