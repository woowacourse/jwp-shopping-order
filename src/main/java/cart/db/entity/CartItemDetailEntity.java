package cart.db.entity;

public class CartItemDetailEntity {

    private final Long id;
    private final Long memberId;
    private final String memberName;
    private final String memberPassword;
    private final Long productId;
    private final String productName;
    private final Integer productPrice;
    private final String productImageUrl;
    private final Integer quantity;

    public CartItemDetailEntity(
            final Long id,
            final Long memberId,
            final String memberName,
            final String memberPassword,
            final Long productId,
            final String productName,
            final Integer productPrice,
            final String productImageUrl,
            final Integer quantity
    ) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
