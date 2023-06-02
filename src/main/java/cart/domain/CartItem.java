package cart.domain;

import cart.exception.IllegalMemberException;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Member member, final Product product) {
        this(null, 1, product, member);
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMemberException("아이템의 소유자와 로그인한 사용자가 일치하지 않습니다.");
        }
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
}
