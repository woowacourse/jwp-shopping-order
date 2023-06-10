package cart.dto.cartitem;

import cart.dao.dto.PageInfo;
import cart.domain.CartItems;
import cart.dto.page.PageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class CartItemsResponse {
    private final List<CartItemResponse> cartItems;
    private final PageResponse pagination;

    public CartItemsResponse(final List<CartItemResponse> cartItems, final PageResponse pagination) {
        this.cartItems = cartItems;
        this.pagination = pagination;
    }

    public static CartItemsResponse of(final CartItems cartItems, final PageInfo pageInfo) {
        List<CartItemResponse> cartItemResponses = cartItems.getItems().stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
        PageResponse pageResponse = PageResponse.from(pageInfo);
        return new CartItemsResponse(cartItemResponses, pageResponse);
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public PageResponse getPagination() {
        return pagination;
    }
}
