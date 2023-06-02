package cart.exception.notfound;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException(final Long id) {
        super(id, "cartItem");
    }
}
