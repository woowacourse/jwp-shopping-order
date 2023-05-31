package cart.domain;

public class OrderDetail {
    private final Product product;
    private final Long quantity;

    public OrderDetail(final Product product, final Long quantity) {
        validateMinimum(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validateMinimum(final Long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 최소 0개여야 합니다");
        }
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
