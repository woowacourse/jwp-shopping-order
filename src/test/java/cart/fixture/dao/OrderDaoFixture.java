package cart.fixture.dao;

import cart.dao.OrderDao;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.OrderHistory;
import cart.exception.OrderException;

import java.time.LocalDateTime;

import static cart.fixture.entity.OrderEntityFixture.주문_엔티티;

public class OrderDaoFixture {

    private final OrderDao orderDao;

    public OrderDaoFixture(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public Long 주문을_등록한다(Member 회원, CartItems 장바구니_상품_목록, String 사용한_포인트, String 배송비, LocalDateTime 주문_시간) {
        return orderDao.insertOrder(주문_엔티티(회원.getId(), 장바구니_상품_목록.totalPrice().getValue().toString(), 사용한_포인트, 배송비, 주문_시간));
    }

    public OrderHistory 주문을_조회한다(Long 주문_식별자값) {
        return orderDao.getByOrderId(주문_식별자값)
                .orElseThrow(OrderException.NotFound::new);
    }
}
