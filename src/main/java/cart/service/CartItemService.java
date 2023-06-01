package cart.service;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.dto.CartItemPriceResponse;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<CartItemPriceResponse> applyCoupon(Member member, Coupon coupon) {
        Coupons coupons = new Coupons(couponDao.findByMemberId(member.getId()));

        validateCoupon(coupons, coupon);

        List<CartItemPriceResponse> cartItemPrice = new ArrayList<>();
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        for (CartItem cartItem : cartItems) {
            Price originalPrice = cartItem.getPrice();
            Price discountedPrice = cartItem.applyCoupon(coupon);
            cartItemPrice.add(new CartItemPriceResponse(
                    cartItem.getId(), originalPrice, originalPrice.minus(discountedPrice)));
        }

        return cartItemPrice;
    }

    private void validateCoupon(Coupons coupons, Coupon coupon) {
        if (!coupons.isExist(coupon)) {
            throw new NotFoundException("회원에게 쿠폰이 존재하지 않습니다.");
        }
    }
}

