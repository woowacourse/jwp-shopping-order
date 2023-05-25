package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private static final int MINIMUM_QUANTITY = 1;

    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = 1;

        validateMember(member);
        this.member = member;

        validateProduct(product);
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        validateId(id);
        this.id = id;

        validateQuantity(quantity);
        this.quantity = quantity;

        validateProduct(product);
        this.product = product;

        validateMember(member);
        this.member = member;
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new CartItemException.IllegalMember("장바구니에 접근하려는 멤버가 없습니다.");
        }
    }

    private void validateProduct(Object object) {
        if (Objects.isNull(object)) {
            throw new CartItemException.IllegalProduct("장바구니에 담으려는 상품이 존재하지 않습니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new CartItemException.IllegalQuantity("장바구니에 담긴 상품의 개수는 최소 " + MINIMUM_QUANTITY + " 이상이어야 합니다.");
        }
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new CartItemException.IllegalId("장바구니 아이디를 입력해야 합니다.");
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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
