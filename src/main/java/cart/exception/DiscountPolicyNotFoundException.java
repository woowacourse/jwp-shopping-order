package cart.exception;

public class DiscountPolicyNotFoundException extends CartException {
    public DiscountPolicyNotFoundException() {
        super("할인 정책을 찾을 수 없습니다.");
    }
}
