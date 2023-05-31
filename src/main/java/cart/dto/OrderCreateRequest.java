package cart.dto;

import java.util.List;

public class OrderCreateRequest {

    private List<Long> cartItemIds;
    int totalItemDiscountAmount;
    int totalMemberDiscountAmount;
    int totalItemPrice;
    int discountedTotalItemPrice;
    int shippingFee;
    int totalPrice;

    //private List<OrderItemRequest> orderItemRequests;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(List<Long> cartItemIds, int totalItemDiscountAmount, int totalMemberDiscountAmount, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, int totalPrice) {
        this.cartItemIds = cartItemIds;
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    //    public OrderCreateRequest(List<OrderItemRequest> orderItemRequests) {
//        this.orderItemRequests = orderItemRequests;
//    }
//
//    public List<OrderItemRequest> getOrderItemRequests() {
//        return orderItemRequests;
//    }


    public List<Long> getCartItemIds() {
        return cartItemIds;
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
