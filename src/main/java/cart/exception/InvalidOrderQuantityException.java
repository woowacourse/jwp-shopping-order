package cart.exception;

public class InvalidOrderQuantityException extends RuntimeException {
	public InvalidOrderQuantityException(){
		super("장바구니의 등록된 상품의 수량과 일치하지 않습니다");
	}
}
