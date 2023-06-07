package cart.dto.request;

public class CartItemInfoRequest {

    private final Long cartItemId;
    private final Integer quantity;
    private final ProductInfoRequest product;

    private CartItemInfoRequest() {
        this(null, null, null);
    }

    public CartItemInfoRequest(Long cartItemId, Integer quantity, ProductInfoRequest product) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductInfoRequest getProduct() {
        return product;
    }
}
