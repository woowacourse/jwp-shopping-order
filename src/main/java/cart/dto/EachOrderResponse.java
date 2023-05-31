package cart.dto;

import java.util.List;

public class EachOrderResponse {
    private List<OrderedProduct> orderedProducts;
    private Long totalPayment;
    private Long point;

    public EachOrderResponse() {
    }

    public EachOrderResponse(final List<OrderedProduct> orderedProducts, final Long totalPayment, final Long point) {
        this.orderedProducts = orderedProducts;
        this.totalPayment = totalPayment;
        this.point = point;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Long getTotalPayment() {
        return totalPayment;
    }

    public Long getPoint() {
        return point;
    }
}
