package cart.application;

import cart.dao.cartitem.CartItemDao;
import cart.dao.product.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findAllCartItemsByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productDao.findProductById(cartItemRequest.getProductId())
                .orElseThrow(() -> new CartException(ErrorCode.PRODUCT_NOT_FOUND));
        cartItemDao.findCartItemByMemberIdAndProductId(member.getId(), cartItemRequest.getProductId())
                .ifPresent(cartItem -> {
                    throw new CartException(ErrorCode.CART_ITEM_DUPLICATED);
                });
        return cartItemDao.save(new CartItem(member, product));
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        List<Long> findCartIds = cartItemDao.findAllCartIdsByMemberId(member.getId());
        if (!findCartIds.contains(id)) {
            throw new CartException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        CartItem cartItem = cartItemDao.findCartItemById(id)
                .orElseThrow(() -> new CartException(ErrorCode.PRODUCT_NOT_FOUND));
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }
        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        List<Long> findCartIds = cartItemDao.findAllCartIdsByMemberId(member.getId());
        if (!findCartIds.contains(id)) {
            throw new CartException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        CartItem cartItem = cartItemDao.findCartItemById(id)
                .orElseThrow(() -> new CartException(ErrorCode.PRODUCT_NOT_FOUND));
        cartItem.checkOwner(member);
        cartItemDao.deleteById(id);
    }
}
