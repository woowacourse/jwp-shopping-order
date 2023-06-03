package cart.application.mapper;

import cart.application.dto.cartitem.CartResponse;
import cart.application.dto.product.ProductResponse;
import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.product.Product;
import cart.domain.product.dto.ProductWithId;

public class CartMapper {
    public static CartResponse convertCartItemResponse(final CartItemWithId cartItemWithId) {
        final ProductWithId productWithId = cartItemWithId.getProduct();
        final Product product = productWithId.getProduct();
        return new CartResponse(cartItemWithId.getCartId(), cartItemWithId.getQuantity(),
            new ProductResponse(productWithId.getProductId(), product.getName(), product.getPrice(),
                product.getImageUrl()));
    }
}
