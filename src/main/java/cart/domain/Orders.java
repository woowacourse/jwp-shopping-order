package cart.domain;

import java.util.List;

public class Orders {
    private Long id;
    private Member member;
    private List<CartItem> cartItems;
    private Integer originalPrice;
    private Integer discountPrice;
    private List<Coupon> coupons;

    public Orders(Long id, Member member, List<CartItem> cartItems, Integer originalPrice, Integer discountPrice, List<Coupon> coupons) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.coupons = coupons;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
