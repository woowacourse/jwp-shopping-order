package cart.ui.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderDto {

    private final List<CreateOrderItemDto> createOrderItemDtos;
    private final CreateOrderDiscountDto createOrderDiscountDto;

    public CreateOrderDto(final List<CreateOrderItemDto> createOrderItemDtos, final CreateOrderDiscountDto createOrderDiscountDto) {
        this.createOrderItemDtos = createOrderItemDtos;
        this.createOrderDiscountDto = createOrderDiscountDto;
    }

    public static CreateOrderDto from(final CreateOrderRequest createOrderRequest) {
        List<CreateOrderItemDto> createOrderItems = createOrderRequest.getOrderItems().stream()
                .map(CreateOrderItemDto::from)
                .collect(Collectors.toUnmodifiableList());

        CreateOrderDiscountDto createOrderDiscounts = CreateOrderDiscountDto.from(createOrderRequest.getOrderDiscounts());

        return new CreateOrderDto(createOrderItems, createOrderDiscounts);
    }

    public List<CreateOrderItemDto> getCreateOrderItemDtos() {
        return createOrderItemDtos;
    }

    public CreateOrderDiscountDto getCreateOrderDiscountDto() {
        return createOrderDiscountDto;
    }

}
