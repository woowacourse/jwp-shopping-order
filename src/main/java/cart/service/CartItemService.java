package cart.service;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.coupon.ProductCoupon;
import cart.domain.coupon.SingleCoupon;
import cart.dto.*;
import cart.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final CouponDao couponDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao, CouponDao couponDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.couponDao = couponDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    private List<OrderCartItem> applyCoupon(Member member, Coupon coupon) {
        Coupons coupons = new Coupons(couponDao.findByMemberId(member.getId()));

        validateCoupon(coupons, coupon);

        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream()
                .map(cartItem -> new OrderCartItem(cartItem, coupon))
                .collect(Collectors.toList());
    }

    private List<OrderCartItem> notApplyCoupon(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream()
                .map(OrderCartItem::new)
                .collect(Collectors.toList());
    }

    public Order applyCoupons(Member member, List<Long> couponIds) {
        List<Long> notNullCouponIds = couponIds.stream().
                filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (notNullCouponIds.size() == 0) {
            List<OrderCartItem> orderCartItems = notApplyCoupon(member);
            return new Order(orderCartItems);
        }

        Coupons coupons = new Coupons(notNullCouponIds.stream()
                .map(couponDao::getCouponById)
                .collect(Collectors.toList()));

        List<Coupon> productCoupons = coupons.findCoupons(ProductCoupon.CATEGORY);
        List<OrderCartItem> orderCartItems = applyProductCoupon(member, productCoupons);

        List<Coupon> singleCoupons = coupons.findCoupons(SingleCoupon.CATEGORY);
        return getOrder(orderCartItems, singleCoupons);
    }

    private Order getOrder(List<OrderCartItem> orderCartItems, List<Coupon> singleCoupons) {
        if (singleCoupons.size() == 0) {
            return new Order(orderCartItems);
        }

        if (singleCoupons.size() == 1) {
            return new Order(orderCartItems, singleCoupons.get(0));
        }

        throw new IllegalArgumentException("중복할인이 불가능합니다. (모든항목)");
    }

    private List<OrderCartItem> applyProductCoupon(Member member, List<Coupon> productCoupons) {
        if (productCoupons.size() == 0) {
            return notApplyCoupon(member);
        }

        if (productCoupons.size() == 1) {
            return applyCoupon(member, productCoupons.get(0));
        }

        throw new IllegalArgumentException("중복할인이 불가능합니다.(상품할인)");
    }

    private void validateCoupon(Coupons coupons, Coupon coupon) {
        if (!coupons.isExist(coupon)) {
            throw new NotFoundException("회원에게 쿠폰이 존재하지 않습니다.");
        }
    }
}

