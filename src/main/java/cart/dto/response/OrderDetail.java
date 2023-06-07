package cart.dto.response;

public class OrderDetail {
    private OrderInfo order;
    private int totalPrice;

    public OrderDetail() {
    }

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
