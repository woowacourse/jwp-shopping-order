package cart.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    @NotNull
    private Long totalProductsPrice;
    @NotNull
    private Long shippingFee;
    private List<OrderItemDto> order;

    public OrderRequest() {
    }

    public OrderRequest(final Long totalProductsPrice, final Long shippingFee, final List<OrderItemDto> order) {
        this.totalProductsPrice = totalProductsPrice;
        this.shippingFee = shippingFee;
        this.order = order;
    }

    public Long getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public List<OrderItemDto> getOrder() {
        return order;
    }
}
