package cart.exception;

public class InvalidOrderProductException extends RuntimeException {
	public InvalidOrderProductException() {
		super("등록된 장바구니의 상품과 일치하지 않습니다");
	}
}
