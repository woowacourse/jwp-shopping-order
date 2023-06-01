package cart.repository.dto;

import cart.domain.Product;

public class OrderAndMainProductDto {

    private final OrderInfoDto orderInfoDto;
    private final Product mainProduct;
    private final int extraProductCount;

    public OrderAndMainProductDto(final OrderInfoDto orderInfoDto, final Product mainProduct, final int extraProductCount) {
        this.orderInfoDto = orderInfoDto;
        this.mainProduct = mainProduct;
        this.extraProductCount = extraProductCount;
    }

    public OrderInfoDto getOrderInfoDto() {
        return orderInfoDto;
    }

    public Product getMainProduct() {
        return mainProduct;
    }

    public int getExtraProductCount() {
        return extraProductCount;
    }
}
