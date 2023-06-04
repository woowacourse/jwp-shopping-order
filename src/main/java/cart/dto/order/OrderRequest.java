package cart.dto.order;

import java.util.List;

public class OrderRequest {
    private Long totalProductsPrice;
    private Long shippingFee;
    private Long usedPoint;
    private List<OrderItemDto> order;

    public OrderRequest() {
    }

    public OrderRequest(Long totalProductsPrice, Long shippingFee, Long usedPoint, List<OrderItemDto> order) {
        this.totalProductsPrice = totalProductsPrice;
        this.shippingFee = shippingFee;
        this.usedPoint = usedPoint;
        this.order = order;
    }

    public Long getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public List<OrderItemDto> getOrder() {
        return order;
    }
}
