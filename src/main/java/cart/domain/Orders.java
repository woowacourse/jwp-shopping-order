package cart.domain;

import java.util.List;

public class Orders {
    private Long id;
    private Member member;
    private List<CartItem> cartItems;
    private Price originalPrice;
    private Price discountPrice;
    private List<Coupon> coupons;
    private boolean confirmState;

    public Orders(Long id, Member member, List<CartItem> cartItems, Integer originalPrice, Integer discountPrice, List<Coupon> coupons, boolean confirmState) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.originalPrice = new Price(originalPrice);
        this.discountPrice = new Price(discountPrice);
        this.coupons = coupons;
        this.confirmState = confirmState;
    }
    private Orders(Member member,  Integer originalPrice, Integer discountPrice){
        this.member = member;
        this.originalPrice = new Price(originalPrice);
        this.discountPrice = new Price(discountPrice);
    }
    public static Orders forIssuer(Member member,  Integer originalPrice, Integer discountPrice){
        return new Orders(member,originalPrice,discountPrice);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }
    public Long getMemberId(){
        return member.getId();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Price getOriginalPrice() {
        return originalPrice;
    }

    public Price getDiscountPrice() {
        return discountPrice;
    }

    public Integer getOriginalPriceValue() {
        return originalPrice.getValue();
    }

    public Integer getDiscountPriceValue() {
        return discountPrice.getValue();
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public boolean isConfirmState() {
        return confirmState;
    }
}
