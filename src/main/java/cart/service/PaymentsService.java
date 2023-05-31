package cart.service;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.domain.*;
import cart.dto.CouponResponse;
import cart.dto.PaymentsResponse;
import cart.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentsService {
    private final CouponDao couponDao;
    private final CartItemDao cartItemDao;

    public PaymentsService(CouponDao couponDao, CartItemDao cartItemDao) {
        this.couponDao = couponDao;
        this.cartItemDao = cartItemDao;
    }

    public PaymentsResponse getPayments(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        List<ProductResponse> productResponses = cartItems.stream()
                .map(cartItem -> cartItem.getProduct())
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        List<Coupon> coupons = couponDao.findByMemberId(member.getId());
        List<CouponResponse> couponResponses = coupons.stream()
                .map(CouponResponse::of)
                .collect(Collectors.toList());

        return new PaymentsResponse(productResponses, couponResponses, Price.DELIVERY);
    }

}

