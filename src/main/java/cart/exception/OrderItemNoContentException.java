package cart.exception;

// TODO: 삭제 구현 하면 RuntimeException 으로 바꾸기
public class OrderItemNoContentException extends IllegalArgumentException {

    private static final String MESSAGE = "주문 상품 목록이 비어있습니다.";

    public OrderItemNoContentException() {
        super(MESSAGE);
    }
}
