package cart.dao.dto.cart;

public class CartItemDto {

    private Long id;
    private final long memberId;
    private final long productId;
    private final int quantity;

    public CartItemDto(long memberId, long productId, int quantity) {
        this(null, memberId, productId, quantity);
    }

    public CartItemDto(Long id, long memberId, long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
