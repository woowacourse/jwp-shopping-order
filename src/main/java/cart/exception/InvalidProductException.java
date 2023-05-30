package cart.exception;

public class InvalidProductException extends RuntimeException {
	public InvalidProductException(){
		super("장바구니에 등록한 상품과 일치하지 않습니다");
	}
}
