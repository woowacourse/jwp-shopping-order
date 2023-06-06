package cart.domain;

import cart.domain.vo.Amount;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartItems {

    private final List<CartItem> value;

    public CartItems(final List<CartItem> value) {
        this.value = value;
    }

    public boolean notContainsExactly(final Map<Long, Integer> productIdAndQuantities) {
        for (final Entry<Long, Integer> entry : productIdAndQuantities.entrySet()) {
            final Long productId = entry.getKey();
            final Integer quantity = entry.getValue();
            final Optional<CartItem> optionalCartItem = value.stream()
                .filter(it -> it.getId().equals(productId) && it.getQuantity() == quantity)
                .findAny();
            if (optionalCartItem.isEmpty()) {
                return true;
            }
        }
        return value.size() != productIdAndQuantities.size();
    }

    public boolean isTotalAmountNotEquals(final Amount amount) {
        final List<Amount> amounts = value.stream()
            .map(it -> it.getProduct().getAmount().multiply(it.getQuantity()))
            .collect(Collectors.toList());
        final Amount totalSum = Amount.of(amounts);
        return !totalSum.equals(amount);
    }

    public List<Long> findAllIds() {
        return value.stream()
            .map(CartItem::getId)
            .collect(Collectors.toUnmodifiableList());
    }

    public Products makeProductsReflectQuantity() {
        final List<Product> products = value.stream()
            .flatMap(cartItem -> makeProductsByQuantity(cartItem).stream())
            .collect(Collectors.toList());
        return new Products(products);
    }

    private List<Product> makeProductsByQuantity(final CartItem cartItem) {
        final ArrayList<Product> products = new ArrayList<>();
        for (int count = 0; count < cartItem.getQuantity(); count++) {
            products.add(cartItem.getProduct());
        }
        return products;
    }

    public Products getProducts() {
        return new Products(value.stream()
            .map(CartItem::getProduct)
            .collect(Collectors.toList()));
    }

    public List<CartItem> getValue() {
        return value;
    }
}
