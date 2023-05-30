package cart.application.mapper;

import cart.application.dto.product.ProductRequest;
import cart.application.dto.product.ProductResponse;
import cart.domain.cartitem.CartItemWithId;
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

    public static Product convertProduct(final ProductRequest productRequest) {
        return new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
    }

    public static ProductResponse convertProductResponse(final Long productId, final Product product) {
        return new ProductResponse(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static ProductResponse convertProductResponse(final ProductWithId productWithId) {
        return new ProductResponse(productWithId.getId(), productWithId.getProduct().getName(),
            productWithId.getProduct().getPrice(), productWithId.getProduct().getImageUrl());
    }
}
