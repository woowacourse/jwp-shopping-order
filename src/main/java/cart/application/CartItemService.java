package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.CartItemException.IllegalId;
import java.util.Objects;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {

    private static final int QUANTITY_MAX = 10;

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        validateDuplicate(member.getId(), cartItemRequest.getProductId());
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    private void validateDuplicate(Long memberId, Long productId) {
        if (cartItemDao.isExist(memberId, productId)) {
            throw new IllegalArgumentException("이미 장바구니에 존재하는 상품입니다..");
        }
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        int quantity = request.getQuantity();
        validateQuantityMax(quantity);

        if (quantity == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(cartItem);
    }

    private void validateQuantityMax(int quantity) {
        if (quantity > QUANTITY_MAX) {
            throw new CartItemException.IllegalQuantity(quantity, QUANTITY_MAX);
        }
    }

    public void remove(Member member, List<Long> ids) {
        List<CartItem> cartItems = cartItemDao.findByIds(ids);
        for (Long id : ids) {
            validateId(member, id, cartItems);
        }
        cartItemDao.deleteByIds(ids);
    }

    private void validateId(Member member, Long id, List<CartItem> cartItems) {
        CartItem cartItem = cartItems.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalId(id));
        cartItem.checkOwner(member);
    }
}
