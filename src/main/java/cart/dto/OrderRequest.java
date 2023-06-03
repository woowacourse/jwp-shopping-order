package cart.dto;

import java.util.List;

public class OrderRequest {

    private int usedPoint;
    private List<ProductOrderRequest> products;

    public OrderRequest() {
    }

    public OrderRequest(int usedPoint, List<ProductOrderRequest> products) {
        this.usedPoint = usedPoint;
        this.products = products;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public List<ProductOrderRequest> getProducts() {
        return products;
    }
}
