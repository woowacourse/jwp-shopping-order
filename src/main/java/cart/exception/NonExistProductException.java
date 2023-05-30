package cart.exception;

public class NonExistProductException extends RuntimeException {

    public NonExistProductException() {
        super("존재하지 않는 상품입니다.");
    }
}
