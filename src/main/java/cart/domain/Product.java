package cart.domain;

import cart.exception.InsufficientStockException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Product {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int stock;

    public Product(final String name, final int price, final String imageUrl, final int stock) {
        this(null, name, price, imageUrl, stock);
    }

    public Product updateStock(final int quantity) {
        validateStock(quantity);

        return new Product(id, name, price, imageUrl, stock - quantity);
    }

    private void validateStock(final int quantity) {
        if (stock < quantity) {
            throw new InsufficientStockException();
        }
    }
}
