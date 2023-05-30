package cart.domain;

import cart.domain.vo.Amount;
import java.util.List;
import java.util.stream.Collectors;

public class Products {

    private final List<Product> value;

    public Products(final List<Product> value) {
        this.value = value;
    }

    public Amount calculateTotalAmount() {
        final List<Amount> amounts = value.stream()
            .map(Product::getAmount)
            .collect(Collectors.toList());
        return Amount.of(amounts);
    }
}
