package com.woowahan.techcourse.cart.application;

import com.woowahan.techcourse.cart.dao.CartItemDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartQueryService {

    private final CartItemDao cartItemDao;

    public CartQueryService(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

}
