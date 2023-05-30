package cart.exception;

public class NotCheckedException extends RuntimeException {
	public NotCheckedException(){
		super("장바구니의 체크된 상품이 아닙니다");
	}
}
