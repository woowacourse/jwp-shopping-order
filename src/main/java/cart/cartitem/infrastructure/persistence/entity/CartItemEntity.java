package cart.cartitem.infrastructure.persistence.entity;

public class CartItemEntity {

    private Long id;
    private int quantity;
    private Long productId;
    private String name;
    private String imageUrl;
    private int productPrice;
    private Long memberId;

    CartItemEntity() {
    }

    public CartItemEntity(
            Long id,
            int quantity,
            Long productId,
            String name,
            String imageUrl,
            int productPrice,
            Long memberId
    ) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.productPrice = productPrice;
        this.memberId = memberId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public Long getMemberId() {
        return memberId;
    }
}
