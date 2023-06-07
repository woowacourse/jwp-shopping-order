package cart.exception.bill;

public class InvalidBillRequestException extends BillException {

    private final static String INVALID_BILL_REQUEST_EXCEPTION_MESSAGE = "주문 요청 정보가 올바르지 않습니다.";

    public InvalidBillRequestException() {
        super(INVALID_BILL_REQUEST_EXCEPTION_MESSAGE);
    }
}
