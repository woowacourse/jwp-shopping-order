package cart.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    @NotNull
    private Long totalProductsPrice;
    @NotNull
    private Long shippingFee;
    @NotNull
    @Min(value = 0, message = "잘못된 요청입니다")
    private Long usedPoint;
    private List<OrderItemDto> order;

    public OrderRequest() {
    }

    public OrderRequest(final Long totalProductsPrice, final Long shippingFee, final Long usedPoint, final List<OrderItemDto> order) {
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
