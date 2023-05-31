package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private Long id;
    private QuantityVO quantityVo;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this(null, 1, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.product = product;
        this.member = member;
        validateQuantityUnderStock(quantity);
        this.quantityVo = new QuantityVO(quantity);
    }

    private void validateQuantityUnderStock(int quantity) {
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("장바구니의 수량은 재고 이하여야 합니다.");
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
        return quantityVo.getQuantity();
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        validateQuantityUnderStock(quantity);
        this.quantityVo = new QuantityVO(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
