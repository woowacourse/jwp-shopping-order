package shop.application.order.dto;

import java.util.List;

public class OrderCreationDto {
    private List<OrderProductDto> orderProduct;
    private Long couponId;

    private OrderCreationDto() {
    }

    private OrderCreationDto(List<OrderProductDto> orderProduct, Long couponId) {
        this.orderProduct = orderProduct;
        this.couponId = couponId;
    }

    public static OrderCreationDto of(List<OrderProductDto> orderProductDtos, Long couponId) {
        return new OrderCreationDto(orderProductDtos, couponId);
    }

    public List<OrderProductDto> getOrderItemDtos() {
        return orderProduct;
    }

    public Long getCouponId() {
        return couponId;
    }
}
