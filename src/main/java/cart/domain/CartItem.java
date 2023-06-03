package cart.domain;

import cart.exception.CartItemException;
import cart.exception.IllegalMemberException;

import java.util.Objects;

public class CartItem {

    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

    public CartItem(int quantity, Member member, Product product) {
        this.quantity = quantity;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        checkPositive(quantity);
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    private void checkPositive(int quantity) {
        if (quantity < 1) {
            throw new CartItemException.InvalidQuantity("1개부터 주문이 가능합니다");
        }
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMemberException("접근할 수 없는 자원입니다 현재 사용자: " + member.getId() + " 장바구니 상품: " + id);
        }
    }

    public long getPrice() {
        return (long) quantity * product.getPrice();
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

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
