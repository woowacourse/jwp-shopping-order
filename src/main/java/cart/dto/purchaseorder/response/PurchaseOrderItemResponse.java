package cart.dto.purchaseorder.response;

import cart.dto.product.ProductResponse;

import java.util.Objects;

public class PurchaseOrderItemResponse {

    private int quantity;
    private ProductResponse product;

    public PurchaseOrderItemResponse(int quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderItemResponse that = (PurchaseOrderItemResponse) o;
        return quantity == that.quantity && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, product);
    }

    @Override
    public String toString() {
        return "PurchaseOrderItemResponse{" +
                "quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
