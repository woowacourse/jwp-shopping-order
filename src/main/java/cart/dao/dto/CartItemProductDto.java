package cart.dao.dto;

public class CartItemProductDto {

    private final long cartItemId;
    private final long productId;
    private final long memberId;
    private final String email;
    private final int quantity;
    private final String productName;
    private final int price;
    private final String productImageUrl;

    public CartItemProductDto(long cartItemId, long productId, long memberId, String email, int quantity, String productName,
        int price, String productImageUrl) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.memberId = memberId;
        this.email = email;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.productImageUrl = productImageUrl;
    }

    public long getCartItemId() {
        return cartItemId;
    }

    public long getProductId() {
        return productId;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
