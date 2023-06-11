package cart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import cart.exception.CartItemException;

public class CartItem {
    private Long id;
    private final Member member;
    private final Product product;
    private int quantity;
    private final List<MemberCoupon> memberCoupons;

    public CartItem(Member member, Product product) {
        this(null, member, product, 1);
    }

    public CartItem(Long id, Member member, Product product, int quantity) {
        this(id, member, product, quantity, Collections.emptyList());
    }

    public CartItem(Long id, Member member, Product product, int quantity, List<MemberCoupon> memberCoupons) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
        this.memberCoupons = new ArrayList<>(memberCoupons);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void apply(List<MemberCoupon> memberCoupons) {
        for (MemberCoupon memberCoupon : memberCoupons) {
            memberCoupon.use();
            this.memberCoupons.add(memberCoupon);
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

    public List<MemberCoupon> getCoupons() {
        return memberCoupons;
    }

    public Money getPrice() {
        Money price = product.getPrice();
        for (MemberCoupon memberCoupon : memberCoupons) {
            price = memberCoupon.getDiscounted(price);
        }
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CartItem cartItem = (CartItem)o;

        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
