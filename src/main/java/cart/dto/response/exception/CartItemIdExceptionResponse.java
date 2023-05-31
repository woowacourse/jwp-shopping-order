package cart.dto.response.exception;

import java.util.List;

public class CartItemIdExceptionResponse extends ExceptionResponse {

    private List<Long> unknownCartItemIds;

    private CartItemIdExceptionResponse(){
        super();
    }

    public CartItemIdExceptionResponse(final String errorMessage, final List<Long> unknownCartItemIds) {
        super(errorMessage);
        this.unknownCartItemIds = unknownCartItemIds;
    }

    public List<Long> getUnknownCartItemIds() {
        return unknownCartItemIds;
    }
}
