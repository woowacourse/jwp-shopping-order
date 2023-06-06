package com.woowahan.techcourse.cart.application;

import com.woowahan.techcourse.cart.dao.CartItemDao;
import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.cart.dto.CartItemQuantityUpdateRequest;
import com.woowahan.techcourse.cart.dto.CartItemRequest;
import com.woowahan.techcourse.cart.exception.CartItemNotFoundException;
import com.woowahan.techcourse.product.application.ProductQueryService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartCommandService {

    private final ProductQueryService productQueryService;
    private final CartItemDao cartItemDao;

    public CartCommandService(ProductQueryService productQueryService, CartItemDao cartItemDao) {
        this.productQueryService = productQueryService;
        this.cartItemDao = cartItemDao;
    }

    public Long add(long memberId, CartItemRequest cartItemRequest) {
        productQueryService.findById(cartItemRequest.getProductId());
        return cartItemDao.save(new CartItem(cartItemRequest.getProductId(), memberId));
    }

    public void updateQuantity(long memberId, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findByIdAndMemberId(id, memberId)
                .orElseThrow(CartItemNotFoundException::new);
        CartItem updatedItem = cartItem.changeQuantity(request.getQuantity());
        updateOrDeleteCartItem(updatedItem);
    }

    private void updateOrDeleteCartItem(CartItem cartItem) {
        if (needToRemove(cartItem)) {
            cartItemDao.deleteById(cartItem.getId());
            return;
        }
        cartItemDao.update(cartItem);
    }

    private boolean needToRemove(CartItem cartItem) {
        return cartItem.getQuantity() == 0;
    }

    public void remove(long memberId, Long id) {
        cartItemDao.deleteByIdAndMemberId(id, memberId);
    }

    public void removeAllByProductIds(Long memberId, List<Long> productIds) {
        cartItemDao.deleteAll(memberId, productIds);
    }
}
