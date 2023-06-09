package shop.domain.cart;

import shop.domain.member.Member;
import shop.domain.product.Product;
import shop.exception.ShoppingException;

import java.util.Objects;

public class CartItem {
    private Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = new Quantity(1);
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = new Quantity(quantity);
        this.product = product;
        this.member = member;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new ShoppingException.IllegalAccessException(
                    "장바구니 상품에 대한 접근 권한이 없습니다." +
                            "장바구니 상품 ID : " + id + ", 회원 ID : " + member.getId()
            );
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = new Quantity(quantity);
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getMemberId() {
        return member.getId();
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
        return quantity.getQuantity();
    }
}
