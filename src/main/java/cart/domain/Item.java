package cart.domain;

import cart.exception.IllegalMemberException;
import java.util.Objects;

public abstract class Item {

    protected final Long id;
    protected int quantity;
    protected final Product product;
    protected final Member member;

    public Item(Long id, int quantity, Product product, Member member) {
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
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMemberException("아이템의 소유자와 로그인한 사용자가 일치하지 않습니다.");
        }
    }
}
