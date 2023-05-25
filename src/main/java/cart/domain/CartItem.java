package cart.domain;

import cart.exception.CartItemException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CartItem {

    private Long id;

    @NotNull
    @Min(0)
    private int quantity;

    @NotNull
    private final Product product;

    @NotNull
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

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
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
