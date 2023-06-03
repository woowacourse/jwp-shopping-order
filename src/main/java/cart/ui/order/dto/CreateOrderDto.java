package cart.ui.order.dto;

import cart.ui.order.dto.request.CreateOrderDiscountRequest;
import cart.ui.order.dto.request.CreateOrderRequest;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderDto {

    private final List<CreateOrderItemDto> createOrderItemDtos;
    private final List<Long> couponIds;
    private final Integer point;

    private CreateOrderDto(List<CreateOrderItemDto> createOrderItemDtos, List<Long> couponIds, Integer point) {
        this.createOrderItemDtos = createOrderItemDtos;
        this.couponIds = couponIds;
        this.point = point;
    }

    public static CreateOrderDto from(CreateOrderRequest createOrderRequest) {
        List<CreateOrderItemDto> createOrderItems = createOrderRequest.getOrderItems().stream()
                .map(CreateOrderItemDto::from)
                .collect(Collectors.toUnmodifiableList());

        CreateOrderDiscountDto createOrderDiscounts = CreateOrderDiscountDto.from(createOrderRequest.getOrderDiscounts());
        CreateOrderDiscountRequest orderDiscounts = createOrderRequest.getOrderDiscounts();
        List<Long> couponIds1 = orderDiscounts.getCouponIds();

        return new CreateOrderDto(createOrderItems, orderDiscounts.getCouponIds(), orderDiscounts.getPoint());
    }

    public List<CreateOrderItemDto> getCreateOrderItemDtos() {
        return createOrderItemDtos;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public Integer getPoint() {
        return point;
    }
}
