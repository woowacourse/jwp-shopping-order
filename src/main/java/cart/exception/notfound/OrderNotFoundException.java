package cart.exception.notfound;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException() {
        super("주문 정보를 찾을 수 없습니다.");
    }
}
