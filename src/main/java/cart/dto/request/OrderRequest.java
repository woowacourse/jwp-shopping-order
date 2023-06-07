package cart.dto.request;

import java.util.List;

public class OrderRequest {

    private final List<CartItemInfoRequest> cartItems;
    private final Integer totalProductPrice;
    private final Integer totalDeliveryFee;
    private final Integer usePoint;
    private final Integer totalPrice;

    private OrderRequest() {
        this(null, null, null, null, null);
    }

    public OrderRequest(
            final List<CartItemInfoRequest> cartItems,
            final Integer totalProductPrice,
            final Integer totalDeliveryFee,
            final Integer usePoint,
            final Integer totalPrice
    ) {
        this.cartItems = cartItems;
        this.totalProductPrice = totalProductPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.usePoint = usePoint;
        this.totalPrice = totalPrice;
    }

    public List<CartItemInfoRequest> getCartItems() {
        return cartItems;
    }

    public Integer getTotalProductPrice() {
        return totalProductPrice;
    }

    public Integer getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public Integer getUsePoint() {
        return usePoint;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
