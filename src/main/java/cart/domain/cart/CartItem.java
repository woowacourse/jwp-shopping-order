package cart.domain.cart;

import cart.domain.product.Product;
import cart.domain.member.Member;
import cart.exception.UnauthorizedAccessException;

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

    public static Builder builder() {
        return new Builder();
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

    public void checkOwner(Long memberId) {
        if (!member.isSameId(memberId)) {
            throw new UnauthorizedAccessException("해당 회원의 장바구니가 아닙니다.");
        }
    }

    public void checkOwner(Member member) {
        checkOwner(member.getId());
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static final class Builder {
        private Long id;
        private int quantity;
        private Product product;
        private Member member;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public CartItem build() {
            return new CartItem(id, quantity, product, member);
        }
    }
}
