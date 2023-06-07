package cart.entity.order;

import cart.dto.history.OrderedProductHistory;

public class ProductHistoryResponse {

    private final long productId;
    private final String productName;
    private final String imgUrl;
    private final int quantity;
    private final int price;

    private ProductHistoryResponse(final long productId, final String productName, String imgUrl, final int quantity, final int price) {
        this.productId = productId;
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public static ProductHistoryResponse from(final OrderedProductHistory orderedProductHistory) {
        return new ProductHistoryResponse(orderedProductHistory.getId(), orderedProductHistory.getProductName(), orderedProductHistory.getImgUrl() , orderedProductHistory.getQuantity(), orderedProductHistory.getPrice());
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
