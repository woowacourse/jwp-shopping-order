package cart.fixture.dao;

import cart.dao.OrderItemDao;
import cart.dao.entity.OrderItemEntity;
import cart.domain.cartitem.CartItem;

import static cart.fixture.entity.OrderItemEntityFixture.주문의_장바구니_상품_엔티티;

public class OrderItemDaoFixture {

    private final OrderItemDao orderItemDao;

    public OrderItemDaoFixture(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    public Long 주문_장바구니_상품을_등록한다(Long 주문_식별자값, CartItem 장바구니_상품) {
        OrderItemEntity 주문의_장바구니_상품_엔티티 = 주문의_장바구니_상품_엔티티(주문_식별자값, 장바구니_상품);
        return orderItemDao.insertOrderItem(주문의_장바구니_상품_엔티티);
    }

}
