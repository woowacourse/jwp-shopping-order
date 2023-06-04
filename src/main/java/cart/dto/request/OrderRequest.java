package cart.dto.request;

import cart.dto.CartItemDto;

import java.util.List;

public class OrderRequest {

    private List<CartItemDto> carItems;
    private Integer deliveryFee;
    private List<Long> couponIds;

    public OrderRequest(List<CartItemDto> carItems, Integer deliveryFee, List<Long> couponIds) {
        this.carItems = carItems;
        this.deliveryFee = deliveryFee;
        this.couponIds = couponIds;
    }

    public List<CartItemDto> getCarItems() {
        return carItems;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }
}
