package cart.service;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.domain.*;
import cart.dto.CouponResponse;
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

    public void applyCoupons(Member member){
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        List<Product> products = cartItems.stream()
                .map(cartItem -> cartItem.getProduct())
                .collect(Collectors.toList());





    }

}

