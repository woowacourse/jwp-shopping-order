package cart.domain.orderproduct;

import cart.domain.cartitem.Quantity;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;

public class OrderProduct {

    private Long id;
    private final Long orderId;
    private final Long productId;
    private final ProductName productName;
    private final ProductPrice productPrice;
    private final ProductImageUrl productImageUrl;
    private final Quantity quantity;

    public OrderProduct(final Long orderId, final Long productId, final ProductName productName,
                        final ProductPrice productPrice, final ProductImageUrl productImageUrl, final Quantity quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductNameValue() {
        return productName.getName();
    }

    public int getProductPriceValue() {
        return productPrice.getPrice();
    }

    public String getProductImageUrlValue() {
        return productImageUrl.getImageUrl();
    }

    public int getQuantityValue() {
        return quantity.getQuantity();
    }
}
