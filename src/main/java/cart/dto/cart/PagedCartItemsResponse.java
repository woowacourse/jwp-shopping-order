package cart.dto.cart;

import cart.domain.cart.CartItem;
import cart.dto.PaginationInfoDto;
import java.util.List;
import org.springframework.data.domain.Page;

public class PagedCartItemsResponse {
    private final List<CartItemResponse> cartItems;
    private final PaginationInfoDto pagination;

    private PagedCartItemsResponse(final List<CartItemResponse> cartItems, final PaginationInfoDto pagination) {
        this.cartItems = cartItems;
        this.pagination = pagination;
    }

    public static PagedCartItemsResponse from(Page<CartItem> pagedCartItems) {
        final List<CartItemResponse> cartItems = CartItemResponse.from(pagedCartItems.getContent());
        final PaginationInfoDto pagination = new PaginationInfoDto((int) pagedCartItems.getTotalElements(),
                pagedCartItems.getSize(),
                pagedCartItems.getNumber(),
                pagedCartItems.getTotalPages());

        return new PagedCartItemsResponse(cartItems, pagination);
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public PaginationInfoDto getPagination() {
        return pagination;
    }
}
