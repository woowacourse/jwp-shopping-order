package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CartItemDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.PriceCalculator;
import cart.dto.request.OrderRequest;
import cart.dto.response.MemberCouponResponse;
import cart.dto.response.MemberCouponsResponse;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CartItemDao cartItemDao;
    private final MemberCouponDao memberCouponDao;

    public OrderService(OrderDao orderDao, OrderItemDao orderItemDao, CartItemDao cartItemDao,
        MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.cartItemDao = cartItemDao;
        this.memberCouponDao = memberCouponDao;
    }

    public MemberCouponsResponse getMemberCoupons(Member member, List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);
        Money price = PriceCalculator.calculate(cartItems);
        List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(member.getId());

        return new MemberCouponsResponse(
            memberCoupons.stream()
                .map(memberCoupon -> new MemberCouponResponse(memberCoupon, price))
                .collect(Collectors.toUnmodifiableList()));
    }

    @Transactional
    public Long add(Member member, OrderRequest orderRequest) {
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        List<CartItem> cartItems = cartItemDao.findByIds(cartItemIds);

        Long memberCouponId = orderRequest.getCouponId();
        Coupon coupon = memberCouponDao.findByIdIfCanBeUsed(member.getId(), memberCouponId);

        Order order = new Order(member, cartItems, coupon);

        Long orderId = orderDao.addOrder(order);
        orderItemDao.saveAllItems(orderId, order);
        memberCouponDao.updateUsabilityById(memberCouponId);
        cartItemDao.deleteByIds(cartItemIds);

        return orderId;
    }
}
