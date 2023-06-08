package cart.service;

import cart.dao.CartItemDao;
import cart.dao.coupon.CouponBoxDao;
import cart.dao.coupon.CouponDao;
import cart.domain.*;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.coupon.ProductCoupon;
import cart.domain.coupon.SingleCoupon;
import cart.domain.order.Order;
import cart.domain.order.OrderCartItem;
import cart.dto.order.OrderRequest;
import cart.dto.order.PreparedOrderResponse;
import cart.exception.DuplicateDiscountException;
import cart.exception.NotFoundException;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CouponBoxDao couponBoxDao;
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;

    public OrderService(OrderRepository orderRepository, CouponBoxDao couponBoxDao,
                        CouponDao couponDao, CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.couponBoxDao = couponBoxDao;
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
    }

    public Long doOrder(Member member, OrderRequest orderRequest) {
        List<Long> notNullCouponIds = orderRequest.getCouponIds().stream().
                filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<CartItem> cartItems = orderRequest.getCartItemIds().stream()
                .map(id -> cartItemDao.findById(id))
                .collect(Collectors.toList());

        Order order = makeOrder(member, notNullCouponIds, cartItems);
        Long id = orderRepository.insert(member, order);

        for (Long cartItemId : orderRequest.getCartItemIds()) {
            cartItemDao.delete(member.getId(), cartItemId);
        }

        for (Long couponId : orderRequest.getCouponIds()) {
            couponBoxDao.delete(member.getId(), couponId);
        }

        return id;
    }

    public PreparedOrderResponse prepareOrder(Member member, List<Long> couponIds) {
        List<Long> notNullCouponIds = couponIds.stream().
                filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        Order order = makeOrder(member, notNullCouponIds, cartItems);
        return PreparedOrderResponse.from(order);
    }

    private Order makeOrder(Member member, List<Long> notNullCouponIds, List<CartItem> cartItems) {
        if (notNullCouponIds.size() == 0) {
            List<OrderCartItem> orderCartItems = notApplyCoupon(cartItems);
            return new Order(orderCartItems);
        }

        List<Coupon> usingCoupons = notNullCouponIds.stream()
                .map(couponDao::getCouponById)
                .collect(Collectors.toList());

        validateCoupons(usingCoupons, couponDao.findByMemberId(member.getId()));

        Coupons coupons = new Coupons(usingCoupons);

        List<Coupon> productCoupons = coupons.findCoupons(ProductCoupon.CATEGORY);
        List<OrderCartItem> orderCartItems = applyProductCoupon(cartItems, productCoupons);

        List<Coupon> singleCoupons = coupons.findCoupons(SingleCoupon.CATEGORY);
        return selectOrder(orderCartItems, singleCoupons);
    }

    private void validateCoupons(List<Coupon> usingCoupons, List<Coupon> memberCoupons) {
        if (usingCoupons.stream()
                .noneMatch(coupon -> memberCoupons.contains(coupon))) {
            throw new NotFoundException("회원에게 쿠폰이 존재하지 않습니다.");
        }
    }

    private List<OrderCartItem> applyCoupon(List<CartItem> cartItems, Coupon coupon) {
        return cartItems.stream()
                .map(cartItem -> new OrderCartItem(cartItem, coupon))
                .collect(Collectors.toList());
    }

    private List<OrderCartItem> notApplyCoupon(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(OrderCartItem::new)
                .collect(Collectors.toList());
    }

    private Order selectOrder(List<OrderCartItem> orderCartItems, List<Coupon> singleCoupons) {
        if (singleCoupons.size() == 0) {
            return new Order(orderCartItems);
        }

        if (singleCoupons.size() == 1) {
            return new Order(orderCartItems, singleCoupons.get(0));
        }

        throw new DuplicateDiscountException("중복할인이 불가능합니다. (모든상품할인)");
    }

    private List<OrderCartItem> applyProductCoupon(List<CartItem> cartItems, List<Coupon> productCoupons) {
        if (productCoupons.size() == 0) {
            return notApplyCoupon(cartItems);
        }

        if (productCoupons.size() == 1) {
            return applyCoupon(cartItems, productCoupons.get(0));
        }

        throw new DuplicateDiscountException("중복할인이 불가능합니다.(단순상품할인)");
    }
}


