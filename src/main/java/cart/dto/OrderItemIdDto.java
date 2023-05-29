package cart.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.beans.ConstructorProperties;

@Schema(description = "주문 상품 id")
public class OrderItemIdDto {

    @Schema(description = "주문 상품 id", example = "1")
    private final Long id;

    @ConstructorProperties("id")
    public OrderItemIdDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
