package cart.exception.notFound;

public class PaymentNotFoundException extends NotFoundException{
    private static final String MESSAGE = "해당 payment가 존재하지 않습니다.";

    public PaymentNotFoundException() {
        super(MESSAGE);
    }
}
