package cart.exception;

import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.domain.product.Product;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember(Order order, Member member) {
            super("Illegal member attempts to order; orderId=" + order.getId() + ", memberId=" + member.getId());
        }
    }

    public static class NoCartItem extends OrderException {
        public NoCartItem(){
            super("주문하려는 상품이 없습니다.");
        }
    }

    public static class ProductPriceUpdated extends OrderException {
        public ProductPriceUpdated(Product product) {
            super("주문하려는 상품의 가격이 변경되었습니다. 달라진 상품 아이디 : " + product.getId() + " 이름 : " + product.getName());
        }
    }

    public static class QuantityNotMatched extends OrderException {
        public QuantityNotMatched(final CartItem actualCartItem) {
            super("주문하려는 상품의 수량을 다시 확인해 주세요. 수량이 변경된 상품 아이디 : " + actualCartItem.getProduct().getId() + " 이름 : " + actualCartItem.getProduct().getName());
        }
    }
}
