package cart.dto.cart;

import cart.dto.PaginationInfoDto;
import java.util.List;

public class PagedCartItemsResponse {
    private final List<CartItemResponse> cartItems;
    private final PaginationInfoDto pagination;

    public PagedCartItemsResponse(final List<CartItemResponse> cartItems, final PaginationInfoDto pagination) {
        this.cartItems = cartItems;
        this.pagination = pagination;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public PaginationInfoDto getPagination() {
        return pagination;
    }
}
