package cart.product.ui.response;

import cart.cartitem.domain.CartItem;
import cart.product.application.dto.ProductCartItemDto;
import cart.product.domain.Product;

import java.util.Optional;

public class ProductDetailResponse {

    private final Product product;
    private final CartItemDetailDto cartItem;

    private ProductDetailResponse(final Product product, final CartItemDetailDto cartItem) {
        this.product = product;
        this.cartItem = cartItem;
    }

    public static ProductDetailResponse from(final ProductCartItemDto productCartItemDto) {
        final Product product = productCartItemDto.getProduct();
        final CartItem cartItem = productCartItemDto.getCartItem();

        return Optional.ofNullable(cartItem)
                .map(item -> new CartItemDetailDto(item.getId(), item.getQuantity()))
                .map(cartItemDto -> new ProductDetailResponse(product, cartItemDto))
                .orElse(new ProductDetailResponse(product, null));
    }

    public static ProductDetailResponse of(final Product product, final CartItem cartItem) {
        final Optional<CartItem> nullableCartItem = Optional.ofNullable(cartItem);
        if (nullableCartItem.isEmpty()) {
            return new ProductDetailResponse(product, null);
        }

        final CartItemDetailDto cartItemDto = new CartItemDetailDto(cartItem.getId(), cartItem.getQuantity());
        return new ProductDetailResponse(product, cartItemDto);
    }

    public Product getProduct() {
        return product;
    }

    public CartItemDetailDto getCartItem() {
        return cartItem;
    }
}
