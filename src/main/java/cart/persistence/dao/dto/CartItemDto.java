package cart.persistence.dao.dto;

public class CartItemDto {

    private final Long cartId;
    private final Long memberId;
    private final String memberName;
    private final String memberPassword;
    private final Long productId;
    private final String productName;
    private final String productImageUrl;
    private final int productPrice;
    private final int productQuantity;
    private final boolean productIsDeleted;

    public CartItemDto(final Long cartId, final Long memberId, final String memberName, final String memberPassword,
                       final Long productId, final String productName, final String productImageUrl,
                       final int productPrice, final int productQuantity, final boolean productIsDeleted) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productIsDeleted = productIsDeleted;
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

    public String getMemberPassword() {
        return memberPassword;
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

    public boolean isProductIsDeleted() {
        return productIsDeleted;
    }
}
