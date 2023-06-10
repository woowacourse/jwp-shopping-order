package cart.fixture;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.coupon.EmptyMemberCoupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.ShippingFee;

import java.util.List;

public class OrderFixture {
    public static final OrderItemEntity 주문상품1_엔티티 = new OrderItemEntity(1L, 1L,1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg", 2);
    public static final OrderItemEntity 주문상품2_엔티티 = new OrderItemEntity(2L, 1L,2L, "화성", 200000, "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg", 4);
    public static final OrderItem 주문상품1 = new OrderItem(1L, ProductFixture.지구,2);
    public static final OrderItem 주문상품2 = new OrderItem(2L, ProductFixture.화성,4);
    public static final OrderEntity 주문1_엔티티 = new OrderEntity(1L, 1L, null, 0, 802000, null);
    public static final Order 주문1 = new Order(1L, ShippingFee.NONE, List.of(주문상품1, 주문상품2), new EmptyMemberCoupon(), MemberFixture.라잇);
    public static final OrderItemEntity 주문상품1_아이디_널_엔티티 = new OrderItemEntity(null, 1L,1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg", 2);
    public static final OrderItemEntity 주문상품2_아이디_널_엔티티 = new OrderItemEntity(null, 1L,2L, "화성", 200000, "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg", 4);
    public static final OrderItem 주문상품1_아이디_널 = new OrderItem(null, ProductFixture.지구,2);
    public static final OrderItem 주문상품2_아이디_널 = new OrderItem(null, ProductFixture.화성,4);
    public static final OrderEntity 주문1_아이디_널_엔티티 = new OrderEntity(null, 1L, null, 0, 802000, null);
    public static final Order 주문1_아이디_널 = new Order(null, ShippingFee.NONE, List.of(주문상품1_아이디_널, 주문상품2_아이디_널), new EmptyMemberCoupon(), MemberFixture.라잇);
}
