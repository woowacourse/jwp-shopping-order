package com.woowahan.techcourse.cart.application;

import com.woowahan.techcourse.cart.dao.CartItemDao;
import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.cart.dto.CartItemResponse;
import com.woowahan.techcourse.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartQueryService {

    private final CartItemDao cartItemDao;

    public CartQueryService(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

}
