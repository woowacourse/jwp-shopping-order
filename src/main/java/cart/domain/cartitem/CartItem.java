package cart.domain.cartitem;

import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Product product, final Member member) {
        this(null, 1, product, member);
    }

    public CartItem(final int quantity, final Product product, final Member member) {
        this(null, quantity, product, member);
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public boolean isCorrectQuantity(final Long productId, final Integer quantity) {
        if (this.quantity == quantity && product.isEqualId(productId)) {
            return true;
        }
        throw new IllegalArgumentException("상품의 수량이 일치하지 않습니다.");
    }

    public void validate(final Long productId, final Long memberId, final Integer quantity) {
        if (!(product.isEqualId(productId) && member.isEqualId(memberId))) {
            throw new IllegalArgumentException("장바구니 정보가 일치하지 않습니다.");
        }
        if (this.quantity != quantity) {
            throw new IllegalArgumentException("상품의 수량이 일치하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getProductId() {
        return product.getId();
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

    public int calculatePriceOfQuantity() {
        return product.calculatePriceBy(quantity);
    }

}
