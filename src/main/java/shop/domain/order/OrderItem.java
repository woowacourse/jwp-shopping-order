package shop.domain.order;

import shop.domain.cart.Quantity;
import shop.domain.member.Member;
import shop.domain.product.Product;
import shop.exception.ShoppingException;

import java.util.Objects;

public class OrderItem {
    private final Long id;
    private final Product product;
    private final Quantity quantity;
    private final Member member;

    public OrderItem(Long id, Product product, Quantity quantity, Member member) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.member = member;
    }

    public OrderItem(Product product, Quantity quantity, Member member) {
        this(null, product, quantity, member);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new ShoppingException.IllegalAccessException(
                    "주문 상품에 대한 접근 권한이 없습니다." +
                            "주문 상품 ID : " + id + ", 회원 ID : " + member.getId()
            );
        }
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public long getTotalPrice() {
        return (long) product.getPrice() * quantity.getQuantity();
    }

    public int getQuantity() {
        return quantity.getQuantity();
    }

}
