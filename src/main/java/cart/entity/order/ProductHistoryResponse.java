package cart.entity.order;

import cart.domain.order.ProductHistory;

public class ProductHistoryResponse {

    private final long productId;
    private final String productName;
    private final int quantity;
    private final int price;

    private ProductHistoryResponse(final long productId, final String productName, final int quantity, final int price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public static ProductHistoryResponse from(final ProductHistory productHistory) {
        return new ProductHistoryResponse(productHistory.getId(), productHistory.getProductName(), productHistory.getQuantity(), productHistory.getPrice());
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
