package cart.dto;

import java.util.List;

public class SpecificOrderResponse {

    private List<OrderedProduct> orderedProducts;
    private Integer totalPayment;
    private Integer point;

    public SpecificOrderResponse() {
    }

    public SpecificOrderResponse(final List<OrderedProduct> orderedProducts, final Integer totalPayment, final Integer point) {
        this.orderedProducts = orderedProducts;
        this.totalPayment = totalPayment;
        this.point = point;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getTotalPayment() {
        return totalPayment;
    }

    public Integer getPoint() {
        return point;
    }
}
