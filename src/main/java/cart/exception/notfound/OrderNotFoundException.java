package cart.exception.notfound;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() {
        super("찾을 수 없는 주문입니다.");
    }
}
