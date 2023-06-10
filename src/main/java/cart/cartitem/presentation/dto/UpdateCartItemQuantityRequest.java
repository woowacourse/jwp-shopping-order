package cart.cartitem.presentation.dto;

import cart.cartitem.application.dto.UpdateCartItemQuantityCommand;
import cart.member.domain.Member;

public class UpdateCartItemQuantityRequest {

    private int quantity;

    public UpdateCartItemQuantityRequest() {
    }

    public UpdateCartItemQuantityRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public UpdateCartItemQuantityCommand toCommand(Long id, Member member) {
        return new UpdateCartItemQuantityCommand(id, member, quantity);
    }
}
