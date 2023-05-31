package cart.exception;

import java.util.List;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember() {
            super("잘못된 요청입니다");
        }
    }

    public static class InvalidCartItem extends CartItemException {
        public InvalidCartItem() {
            super("더 이상 존재하지 않는 상품입니다");
        }
    }

    public static class DuplicatedCartItem extends CartItemException {
        public DuplicatedCartItem() {
            super("이미 장바구니에 존재하는 상품입니다");
        }
    }

    public static class UnknownCartItem extends CartItemException {

        private final List<Long> unknownCartItemIds;
        public UnknownCartItem(final List<Long> unknownCartItemIds) {
            super("등록되지 않은 상품이 포함되어 있습니다. 다시 한번 확인해주세요");
            this.unknownCartItemIds = unknownCartItemIds;
        }

        public List<Long> getUnknownCartItemIds() {
            return unknownCartItemIds;
        }
    }

    public static class QuantityNotSame extends CartItemException {

        private final List<Long> strangeQuantityCartItemIds;
        public QuantityNotSame(final List<Long> strangeQuantityCartItemIds) {
            super("문제가 발생했습니다. 상품의 수량을 다시 한번 확인해주세요");
            this.strangeQuantityCartItemIds = strangeQuantityCartItemIds;
        }

        public List<Long> getStrangeQuantityCartItemIds() {
            return strangeQuantityCartItemIds;
        }
    }

    public static class TotalPriceNotSame extends CartItemException {
        public TotalPriceNotSame() {
            super("상품 정보에 변동사항이 존재합니다. 금액을 다시 한번 확인해주세요");
        }
    }
}
