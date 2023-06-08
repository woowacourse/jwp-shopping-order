package cart.domain;

import cart.exception.UnauthorizedAccessException;
import java.util.Objects;

public class CartItem {
    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new UnauthorizedAccessException("해당 회원의 장바구니가 아닙니다.");
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
