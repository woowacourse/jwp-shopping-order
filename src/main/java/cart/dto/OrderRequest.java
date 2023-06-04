package cart.dto;

import java.util.List;

public class OrderRequest {

    private Integer usedPoint;
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
