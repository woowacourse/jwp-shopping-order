package cart.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }

    public static class Product extends NotFoundException {

        public Product(final Long productId) {
            super(productId + "id에 해당하 상품을 찾을 수 없습니다");
        }
    }
    public static class Member extends NotFoundException {

        public Member(final Long id) {
            super(id + "id 에 해당하는 회원이 없습니다.");
        }
        public Member(final String email) {
            super(email + " 이메일을 가진 회원이 없습니다.");
        }

    }
    public static class CartItem extends NotFoundException {

        public CartItem(final Long id) {
            super(id + " id에 해당하는 장바구니가 없습니다.");
        }
    }
    public static class Order extends NotFoundException {

        public Order(final Long id) {
            super(id + " id에 해당하는 주문이 없습니다.");
        }
    }
    public static class MemberNotHavingPoint extends  NotFoundException{
        public MemberNotHavingPoint(final Long memberId) {
            super(memberId + " 멤버는 포인트를 가지고 있지 않습니다.");
        }
    }

    public static class OrderNotHavingPointHistory extends  NotFoundException{
        public OrderNotHavingPointHistory(final Long orderId) {
            super(orderId + " 주문에 해당하는 포인트 내역이 없습니다.");
        }
    }
}
