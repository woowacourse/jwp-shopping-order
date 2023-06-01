package cart.repository.dto;

import cart.entity.CartItemWithProductEntity;

public class CartItemWithProductDto {

    private final long id;
    private final long memberId;
    private final long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    private CartItemWithProductDto(final long id, final long memberId, final long productId,
                                  final String productName, final int productPrice, final String productImageUrl,
                                  final int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public static CartItemWithProductDto from(final CartItemWithProductEntity entity) {
        return new CartItemWithProductDto(
                entity.getId(), entity.getMemberId(), entity.getProductId(), entity.getProductName(),
                entity.getProductPrice(), entity.getProductImageUrl(), entity.getQuantity()
        );
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
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
