package cart.cartitem.application.dto;

import cart.member.domain.Member;

public class AddCartItemCommand {

    private final Member member;
    private final Long productId;

    public AddCartItemCommand(Member member, Long productId) {
        this.member = member;
        this.productId = productId;
    }

    public Member getMember() {
        return member;
    }

    public Long getProductId() {
        return productId;
    }
}
