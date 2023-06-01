package cart.exception;

public class OrderException extends ShoppingException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotFound extends OrderException {

        public NotFound() {
            super("해당 주문이 존재하지 않습니다.");
        }
    }

    public static class IllegalMember extends OrderException {

        public IllegalMember() {
            super("해당 주문을 관리할 수 있는 멤버가 아닙니다.");
        }
    }

    public static class IllegalMinimumQuantity extends OrderException {

        public IllegalMinimumQuantity(int currentQuantity, int limit) {
            super("주문 상품 수량은 최소 " + limit + "개부터 가능합니다. 현재 개수: " + currentQuantity);
        }
    }

    public static class IllegalPoint extends OrderException {

        public IllegalPoint() {
            super("포인트 적용은 0원 이상만 가능합니다.");
        }
    }

    public static class IllegalFee extends OrderException {

        public IllegalFee() {
            super("배송료는 0원 이상만 가능합니다.");
        }
    }

    public static class IllegalPointUse extends OrderException {

        public IllegalPointUse(int totalPrice, int usePoint) {
            super("사용하려는 포인트가 총 결제 금액보다 큽니다. 총 결제 금액: " + totalPrice + ", 사용 포인트: " + usePoint);
        }
    }
}
