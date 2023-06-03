package shop.application.order.dto;

import java.util.List;

public class OrderCreationDto {
    private List<OrderProductDto> orderProductDtos;
    private Long couponId;

    private OrderCreationDto() {
    }

    private OrderCreationDto(List<OrderProductDto> orderProductDtos, Long couponId) {
        this.orderProductDtos = orderProductDtos;
        this.couponId = couponId;
    }

    public static OrderCreationDto of(List<OrderProductDto> orderProductDtos, Long couponId) {
        return new OrderCreationDto(orderProductDtos, couponId);
    }

    public List<OrderProductDto> getOrderItemDtos() {
        return orderProductDtos;
    }

    public Long getCouponId() {
        return couponId;
    }
}
