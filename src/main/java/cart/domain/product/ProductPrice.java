package cart.domain.product;

import java.util.Objects;

public class ProductPrice {

    private final int price;

    public ProductPrice(final int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductPrice other = (ProductPrice) o;
        return price == other.price;
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
