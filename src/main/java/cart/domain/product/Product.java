package cart.domain.product;

import cart.domain.vo.Price;
import cart.domain.vo.Quantity;
import java.util.Objects;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;
    private final String imageUrl;

    public Product(final String name, final Price price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(final Long id, final String name, final Price price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Price calculatePrice(final Quantity quantity) {
        return price.multi(quantity.getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
