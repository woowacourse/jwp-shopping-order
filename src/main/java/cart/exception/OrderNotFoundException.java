package cart.exception;

public class OrderNotFoundException extends RuntimeException{

    public static final String ENTITY_ERROR_MESSAGE = "존재하지 않는 OrderEntity에 접근하였습니다. (접근 id값 : %d)";

    public OrderNotFoundException(Long entityId) {
        super(String.format(ENTITY_ERROR_MESSAGE, entityId));
    }
}
