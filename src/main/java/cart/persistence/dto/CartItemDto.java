package cart.persistence.dto;

public class CartItemDto {

    private final Long cartId;
    private final Long memberId;
    private final String memberName;
    private final Long productId;
    private final String productName;
    private final String productImageUrl;
    private final int productPrice;
    private final int productQuantity;

    public CartItemDto(final Long cartId, final Long memberId, final String memberName, final Long productId,
                       final String productName,
                       final String productImageUrl, final int productPrice, final int productQuantity) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}
