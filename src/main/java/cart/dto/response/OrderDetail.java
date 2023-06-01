package cart.dto.response;

public class OrderDetail {
    private final OrderInfo order;
    private final int totalPrice;

    public OrderDetail(final OrderInfo order, final int totalPrice) {
        this.order = order;
        this.totalPrice = totalPrice;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
