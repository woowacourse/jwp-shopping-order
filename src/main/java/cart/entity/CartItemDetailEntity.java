package cart.entity;

public class CartItemDetailEntity {

    private final long id;
    private final long memberId;
    private final String memberEmail;
    private final long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    public CartItemDetailEntity(final long id, final long memberId, final String memberEmail,
                                final long productId, final String productName, final int productPrice,
                                final String productImageUrl, final int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
