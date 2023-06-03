package cart.dto;

import java.util.Objects;

public class ProductOrderResponse {

    private int quantity;
    private ProductResponse productResponse;

    public ProductOrderResponse(int quantity, ProductResponse productResponse) {
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductOrderResponse that = (ProductOrderResponse) o;
        return quantity == that.quantity && Objects.equals(productResponse, that.productResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, productResponse);
    }

    @Override
    public String toString() {
        return "ProductOrderResponse{" +
                "quantity=" + quantity +
                ", productResponse=" + productResponse +
                '}';
    }
}
