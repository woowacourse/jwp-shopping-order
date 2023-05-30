package cart.cart.presentation.dto;

import cart.cart.domain.cartitem.CartItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemResponse {
    private long productId;
    private String productName;
    private long price;
    private String imgUrl;
    private boolean isOnSale;
    private int salePrice;

    public CartItemResponse() {
    }

    private CartItemResponse(long productId, String productName, long price, String imgUrl, boolean isOnSale, int salePrice) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imgUrl = imgUrl;
        this.isOnSale = isOnSale;
        this.salePrice = salePrice;
    }

    public static CartItemResponse from(CartItem cartItem) {
        return new CartItemResponse(cartItem.getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getDiscountPrice() != 0,
                cartItem.getDiscountPrice());
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public long getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @JsonProperty("isOnSale")
    public boolean isOnSale() {
        return isOnSale;
    }

    public int getSalePrice() {
        return salePrice;
    }
}
