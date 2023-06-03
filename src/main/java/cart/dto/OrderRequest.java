package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderRequest {

    @JsonProperty("cartItemIdList")
    private List<Long> cartItemIds;
    private Long totalPrice;
    private Long deliveryFee;
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItemIds, final Long totalPrice, final Long deliveryFee,
                        final Long couponId) {
        this.cartItemIds = cartItemIds;
        this.totalPrice = totalPrice;
        this.deliveryFee = deliveryFee;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getCouponId() {
        return couponId;
    }
}
