package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니에 담은 상품")
public class ItemIdDto {

    @Schema(description = "상품 아이디")
    private Long id;

    public ItemIdDto() {
    }

    public ItemIdDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
