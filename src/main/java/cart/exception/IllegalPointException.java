package cart.exception;

public class IllegalPointException extends RuntimeException {

    public IllegalPointException(String message) {
        super(message);
    }

    public static class UsingMorePointThanPrice extends IllegalOrderException {
        public UsingMorePointThanPrice() {
            super("지불할 금액을 초과하는 포인트를 사용할 수 없습니다");
        }
    }

    public static class PointIsNotEnough extends IllegalOrderException {
        public PointIsNotEnough() {
            super("보유한 포인트 이상을 사용할 수 없습니다");
        }
    }
}
