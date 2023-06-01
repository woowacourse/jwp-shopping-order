package cart.fixture.entity;

import cart.dao.entity.OrderItemEntity;
import cart.domain.cartitem.CartItem;

public abstract class OrderItemEntityFixture {

    public static OrderItemEntity 주문의_장바구니_상품_엔티티(Long 주문_식별자값, CartItem 장바구니_상품) {
        return OrderItemEntity.from(주문_식별자값, 장바구니_상품);
    }
}
