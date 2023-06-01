package cart.ui.dto;

import java.util.List;

public class OrderCreateRequest {

    private List<CartItemDto> cartItems;
    private int totalItemDiscountAmount;
    private int totalMemberDiscountAmount;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private int totalPrice;

    public OrderCreateRequest() {
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public int getTotalItemDiscountAmount() {
        return totalItemDiscountAmount;
    }

    public int getTotalMemberDiscountAmount() {
        return totalMemberDiscountAmount;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public int getDiscountedTotalItemPrice() {
        return discountedTotalItemPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
