package cart.dto;

import java.util.List;

public class OrderRequest {
    private List<Long> cartIds;
    private int totalPrice;
    private int discountPrice;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartIds, int totalPrice, int discountPrice) {
        this.cartIds = cartIds;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
