package cart.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotNull
    @PositiveOrZero
    private Integer usedPoint;

    @NotNull
    private List<ProductOrderRequest> products;

    public OrderRequest() {
    }

    public OrderRequest(Integer usedPoint, List<ProductOrderRequest> products) {
        this.usedPoint = usedPoint;
        this.products = products;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public List<ProductOrderRequest> getProducts() {
        return products;
    }
}
