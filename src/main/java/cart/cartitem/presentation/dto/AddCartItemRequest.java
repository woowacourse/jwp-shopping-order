package cart.cartitem.presentation.dto;

import cart.cartitem.application.dto.AddCartItemCommand;
import cart.member.domain.Member;

public class AddCartItemRequest {

    private Long productId;

    public AddCartItemRequest() {
    }

    public AddCartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public AddCartItemCommand toCommand(Member member) {
        return new AddCartItemCommand(member, productId);
    }
}
