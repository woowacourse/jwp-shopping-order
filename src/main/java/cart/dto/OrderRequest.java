package cart.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {

    private List<Long> cartItemIds;
    private int deliveryFee;
    private BigDecimal totalOrderPrice;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, int deliveryFee, BigDecimal totalOrderPrice) {
        this.cartItemIds = cartItemIds;
        this.deliveryFee = deliveryFee;
        this.totalOrderPrice = totalOrderPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }


    public int getDeliveryFee() {
        return deliveryFee;
    }

    public BigDecimal getTotalOrderPrice() {
        return totalOrderPrice;
    }
}
