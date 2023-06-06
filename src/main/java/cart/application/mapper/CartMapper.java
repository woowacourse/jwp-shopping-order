package cart.application.mapper;

import cart.application.dto.cartitem.CartResponse;
import cart.application.dto.product.ProductResponse;
import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;

public class CartMapper {

    public static CartResponse convertCartItemResponse(final CartItem cartItem) {
        final Product product = cartItem.getProduct();
        return new CartResponse(cartItem.getCartId(), cartItem.getQuantity(),
            new ProductResponse(product.getProductId(), product.getName(), product.getPrice(),
                product.getImageUrl()));
    }
}
