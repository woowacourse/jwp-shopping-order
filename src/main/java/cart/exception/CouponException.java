package cart.exception;

public class CouponException extends RuntimeException {

    public CouponException(String message) {
        super(message);
    }

    public static class WrongDiscountType extends CouponException {
        public WrongDiscountType(String input) {
            super("잘못된 할인정책 명이 입력되었습니다. - " + input);
        }
    }

    public static class AlreadyUsed extends CouponException {
        public AlreadyUsed() {
            super("이미 사용된 쿠폰입니다.");
        }
    }

    public static class OverOriginalPrice extends CouponException {
        public OverOriginalPrice() {
            super("쿠폰 할인금액이 주문 금액보다 높습니다.");
        }
    }
}
