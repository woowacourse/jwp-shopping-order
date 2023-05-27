package cart.exception;

public class DiscountConditionNotFoundException extends CartException {
    public DiscountConditionNotFoundException() {
        super("할인 조건을 찾을 수 없습니다.");
    }
}
