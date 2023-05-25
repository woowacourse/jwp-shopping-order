package cart.cartitem.application.dto;

import cart.member.domain.Member;

public class UpdateCartItemQuantityCommand {

    private final Long id;
    private final Member member;
    private final int quantity;

    public UpdateCartItemQuantityCommand(Long id, Member member, int quantity) {
        this.id = id;
        this.member = member;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getQuantity() {
        return quantity;
    }
}
