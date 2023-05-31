package cart.persistence;

public class CartItemDetail {
    private final Long id;
    private final Integer quantity;
    private final Long productId;
    private final String productName;
    private final Integer price;
    private final String imageUrl;
    private final Long memberId;
    private final String memberName;
    private final String password;

    public CartItemDetail(Long id, Integer quantity, Long productId, String productName,
                          Integer price, String imageUrl, Long memberId, String memberName, String password) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.memberId = memberId;
        this.memberName = memberName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getPassword() {
        return password;
    }
}
