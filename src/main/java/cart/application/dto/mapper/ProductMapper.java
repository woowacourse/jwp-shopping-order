package cart.application.dto.mapper;

import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.product.Product;
import cart.domain.product.dto.ProductWithId;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static List<ProductWithId> convertProductWithIds(final List<CartItemWithId> cartItems) {
        return cartItems.stream()
            .map(cartItemWithId -> {
                final ProductWithId productWithId = cartItemWithId.getProduct();
                return new ProductWithId(productWithId.getId(), convertProduct(productWithId));
            }).collect(Collectors.toUnmodifiableList());
    }

    public static Product convertProduct(final ProductWithId productWithId) {
        return new Product(productWithId.getProduct().getName(), productWithId.getProduct().getPrice(),
            productWithId.getProduct().getImageUrl());
    }
}
