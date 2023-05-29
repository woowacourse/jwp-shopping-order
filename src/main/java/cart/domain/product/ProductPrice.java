package cart.domain.product;

import java.util.Objects;

public class ProductPrice {

    private final int price;

    public ProductPrice(final int price) {
        validatePositive(price);
        this.price = price;
    }

    private void validatePositive(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("가격은 양수여야합니다.");
        }
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return price == that.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "price=" + price +
                '}';
    }
}
