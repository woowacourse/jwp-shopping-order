package cart.exception.notFound;

public class OrderNotFoundException extends NotFoundException{
    private static final String MESSAGE = "해당 order가 존재하지 않습니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
