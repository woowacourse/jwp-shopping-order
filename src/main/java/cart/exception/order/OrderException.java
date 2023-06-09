package cart.exception.order;

import cart.exception.BadRequestException;

public class OrderException {

    public static class NotExistOrder extends BadRequestException {
        public NotExistOrder() {
            super("존재하지 않는 주문 내역입니다");
        }
    }

    public static class NotSameTotalPrice extends BadRequestException {
        public NotSameTotalPrice() {
            super("상품 정보에 변동사항이 존재합니다. 금액을 다시 한번 확인해주세요");
        }
    }

    public static class MinusOrderPrice extends BadRequestException {
        public MinusOrderPrice() {
            super("잘못된 요청입니다");
        }
    }
}
