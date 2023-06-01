package cart.application;

import cart.dao.cartitem.CartItemDao;
import cart.dao.product.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.exception.customexception.CartItemDuplicatedException;
import cart.exception.customexception.CartItemNotFoundException;
import cart.exception.customexception.ProductNotFoundException;
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
                .orElseThrow(ProductNotFoundException::new);
        cartItemDao.findCartItemByMemberIdAndProductId(member.getId(), cartItemRequest.getProductId())
                .ifPresent(cartItem -> {
                    throw new CartItemDuplicatedException();
                });
        return cartItemDao.save(new CartItem(member, product));
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        List<Long> findCartIds = cartItemDao.findAllCartIdsByMemberId(member.getId());
        if (!findCartIds.contains(id)) {
            throw new CartItemNotFoundException();
        }
        CartItem cartItem = cartItemDao.findCartItemById(id)
                .orElseThrow(ProductNotFoundException::new);
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
            throw new CartItemNotFoundException();
        }
        CartItem cartItem = cartItemDao.findCartItemById(id)
                .orElseThrow(ProductNotFoundException::new);
        cartItem.checkOwner(member);
        cartItemDao.deleteById(id);
    }
}
