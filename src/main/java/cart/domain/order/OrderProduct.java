package cart.domain.order;

import cart.domain.cartitem.Quantity;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;

import java.util.Objects;

public class OrderProduct {

    private static final long NOT_YET_PERSIST_ID = -1;

    private final Long id;
    private final Order order;
    private final Long productId;
    private final ProductName productName;
    private final ProductPrice productPrice;
    private final ProductImageUrl productImageUrl;
    private final Quantity quantity;

    public OrderProduct(final Order order, final long productId, final ProductName productName,
                        final ProductPrice productPrice, final ProductImageUrl productImageUrl, final Quantity quantity) {
        this(NOT_YET_PERSIST_ID, order, productId, productName, productPrice, productImageUrl, quantity);
    }

    public OrderProduct(final long id, final Order order, final long productId, final ProductName productName,
                        final ProductPrice productPrice, final ProductImageUrl productImageUrl, final Quantity quantity) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getProductId() {
        return productId;
    }

    public ProductName getProductName() {
        return productName;
    }

    public String getProductNameValue() {
        return productName.getName();
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public int getProductPriceValue() {
        return productPrice.getPrice();
    }


    public ProductImageUrl getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductImageUrlValue() {
        return productImageUrl.getImageUrl();
    }


    public Quantity getQuantity() {
        return quantity;
    }

    public int getQuantityValue() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderProduct that = (OrderProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", order=" + order +
                ", productName=" + productName +
                ", productPrice=" + productPrice +
                ", productImageUrl=" + productImageUrl +
                ", quantity=" + quantity +
                '}';
    }
}
