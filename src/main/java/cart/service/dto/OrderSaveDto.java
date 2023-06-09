package cart.service.dto;

import cart.controller.dto.OrderRequest;
import org.springframework.lang.Nullable;

import java.util.List;

public class OrderSaveDto {
    private final List<Long> cartItemIds;
    private final int price;
    @Nullable
    private final Long couponId;

    public OrderSaveDto(final List<Long> cartItemIds, final int price, @Nullable final Long couponId) {
        this.cartItemIds = cartItemIds;
        this.price = price;
        this.couponId = couponId;
    }

    public static OrderSaveDto from(final OrderRequest orderRequest) {
        return new OrderSaveDto(orderRequest.getCartItemIds(), orderRequest.getPrice(), orderRequest.getCouponId());
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getPrice() {
        return price;
    }

    @Nullable
    public Long getCouponId() {
        return couponId;
    }
}
