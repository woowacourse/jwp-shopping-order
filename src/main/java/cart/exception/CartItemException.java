package cart.exception;

import java.util.List;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember() {
            super("잘못된 접근입니다.");
        }
    }

    public static class DuplicatedCartItem extends CartItemException {
        public DuplicatedCartItem() {
            super("이미 장바구니에 담긴 상품입니다.");
        }
    }

    public static class UnknownCartItem extends CartItemException {

        private final List<Long> unknownCartItemIds;
        public UnknownCartItem(final List<Long> unknownCartItemIds) {
            super("등록되지 않은 상품이 포함되어 있습니다.");
            this.unknownCartItemIds = unknownCartItemIds;
        }

        public List<Long> getUnknownCartItemIds() {
            return unknownCartItemIds;
        }
    }

    public static class QuantityNotSame extends CartItemException {

        private final List<Long> strangeQuantityCartItemIds;
        public QuantityNotSame(final List<Long> strangeQuantityCartItemIds) {
            super("상품 수량 정보가 서버와 다릅니다.");
            this.strangeQuantityCartItemIds = strangeQuantityCartItemIds;
        }

        public List<Long> getStrangeQuantityCartItemIds() {
            return strangeQuantityCartItemIds;
        }
    }

    public static class TotalPriceNotSame extends CartItemException {
        public TotalPriceNotSame() {
            super("총 가격이 일치하지 않습니다.");
        }
    }
}
