package cart.exception;

public class ConditionTypeNotFoundException extends CartException {
    public ConditionTypeNotFoundException() {
        super("일치하는 할인 조건이 없습니다.");
    }
}
