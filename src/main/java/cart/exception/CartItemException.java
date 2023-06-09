package cart.exception;

public class CartItemException extends RuntimeException {
    public CartItemException(final String message) {
        super(message);
    }

    public static class IllegalQuantity extends CartItemException {
        public IllegalQuantity(final int quantity, final int maxQuantity) {
            super("Illegal cart item quantity; quantity=" + quantity + ", max=" + maxQuantity);
        }
    }

    public static class IllegalId extends CartItemException {
        public IllegalId(final Long id) {
            super("Illegal cart item id; id=" + id);
        }
    }

    public static class ExistingProductId extends CartItemException {
        public ExistingProductId(final Long productId) {
            super("already existing product in cart; product id =" + productId);
        }
    }
}
