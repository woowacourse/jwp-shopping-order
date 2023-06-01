package cart.dto;

import java.util.List;

public class OrderRequest {
    private final List<CartItemDto> cartItems;
    private final Integer totalProductPrice;
    private final Integer totalDeliveryFee;
    private final Integer usePoint;
    private final Integer totalPrice;

    public OrderRequest(List<CartItemDto> cartItems, Integer totalProductPrice, Integer totalDeliveryFee,
                        Integer usePoint,
                        Integer totalPrice) {
        this.cartItems = cartItems;
        this.totalProductPrice = totalProductPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.usePoint = usePoint;
        this.totalPrice = totalPrice;
    }

    public List<CartItemDto> getCartItems() {
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
