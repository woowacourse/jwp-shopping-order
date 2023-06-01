package cart.exception.notfound;

public class OrderNotFoundException extends NotFoundException {

    public OrderNotFoundException(Long id) {
        super("해당 주문이 존재하지 않습니다. 입력된 주문 ID: " + id);
    }
}
