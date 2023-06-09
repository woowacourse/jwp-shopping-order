package cart.application.service.order.dto;

import cart.ui.order.dto.request.CreateOrderRequest;

import java.util.List;

public class CreateOrderDto {

    private final List<CreateOrderItemDto> createOrderItemDtos;
    private final CreateOrderDiscountDto createOrderDiscountDto;

    public CreateOrderDto(final List<CreateOrderItemDto> createOrderItemDtos, final CreateOrderDiscountDto createOrderDiscountDto) {
        this.createOrderItemDtos = createOrderItemDtos;
        this.createOrderDiscountDto = createOrderDiscountDto;
    }

    public static CreateOrderDto from(final CreateOrderRequest createOrderRequest) {
        final List<CreateOrderItemDto> createOrderItems = CreateOrderItemDto.from(createOrderRequest.getOrderItems());

        final CreateOrderDiscountDto createOrderDiscounts = CreateOrderDiscountDto.from(createOrderRequest.getOrderDiscounts());

        return new CreateOrderDto(createOrderItems, createOrderDiscounts);
    }

    public List<CreateOrderItemDto> getCreateOrderItemDtos() {
        return createOrderItemDtos;
    }

    public CreateOrderDiscountDto getCreateOrderDiscountDto() {
        return createOrderDiscountDto;
    }

}
