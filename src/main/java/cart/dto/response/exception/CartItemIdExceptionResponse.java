package cart.dto.response.exception;

import java.util.List;

public class CartItemIdExceptionResponse extends ExceptionResponse {

    private List<Long> cartItemIds;

    private CartItemIdExceptionResponse(){
        super();
    }

    public CartItemIdExceptionResponse(final String errorMessage, final List<Long> cartItemIds) {
        super(errorMessage);
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
