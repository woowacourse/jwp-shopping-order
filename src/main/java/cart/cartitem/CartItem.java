package cart.cartitem;

import cart.cartitem.application.exception.CartItemException;
import cart.member.Member;

import java.util.Objects;

public class CartItem {
    private Long id;
    private String name;
    private int originalPrice;
    private int quantity;
    private int discountPrice;
    private String imageUrl;
    private Long productId;
    private Long memberId;

    public CartItem(Long id, String name, int originalPrice, int quantity, String imageUrl, Long productId, Long memberId) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.memberId = memberId;
    }

    public CartItem(String name, int originalPrice, int quantity, String imageUrl, Long productId, Long memberId) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.memberId, member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isOnSale() {
        return this.discountPrice != 0;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getDiscountedPrice() {
        return this.originalPrice - this.discountPrice;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", originalPrice=" + originalPrice +
                ", quantity=" + quantity +
                ", discountPrice=" + discountPrice +
                ", productId=" + productId +
                ", memberId=" + memberId +
                '}';
    }
}
