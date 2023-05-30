package cart.exception;

public class InvalidQuantityException extends RuntimeException {
	public InvalidQuantityException(){
		super("장바구에 등록한 상품 수량과 일치하지 않습니다");
	}
}
