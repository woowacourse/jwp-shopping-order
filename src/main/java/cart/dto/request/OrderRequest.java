package cart.dto.request;

import cart.dto.CartItemDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "carItems 필드가 있어야 합니다.")
    private List<CartItemDto> carItems;

    @NotNull(message = "deliveryFee 필드가 있어야 합니다.")
    @PositiveOrZero(message = "배달요금은 음수일 수 없습니다.")
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
