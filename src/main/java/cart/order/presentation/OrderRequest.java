package cart.order.presentation;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private List<OrderProductsRequest> products;
    private List<OrderCouponRequest> coupons;

    public OrderRequest() {
    }

    public int findQuantityByCartItemId(Long productId) {
        return products.stream()
                .filter(productRequest -> productRequest.getId().equals(productId))
                .findAny()
                .orElseThrow(IllegalStateException::new)
                .getQuantity();
    }

    public boolean isCartItemOrdered(Long cartItemId) {
        return products.stream()
                .map(OrderProductsRequest::getId)
                .collect(Collectors.toList())
                .contains(cartItemId);
    }

    public List<OrderCouponRequest> getCoupons() {
        return coupons;
    }

    public List<OrderProductsRequest> getProducts() {
        return products;
    }
}
