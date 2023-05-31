package cart.exception;

public class DiscountPolicyNotFoundException extends CartException {
    public DiscountPolicyNotFoundException() {
        super("쿠폰타입을 찾을 수 없습니다.");
    }
}
