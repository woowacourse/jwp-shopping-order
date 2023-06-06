package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.exception.badrequest.cartitem.CartItemDuplicateException;
import cart.exception.notfound.CartItemNotFoundException;
import cart.repository.mapper.CartItemMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public CartItems findAllInIds(List<Long> ids) {
        Set<Long> uniqueIds = new HashSet<>(ids);
        if (uniqueIds.size() != ids.size()) {
            throw new CartItemDuplicateException();
        }

        List<CartItemEntity> cartItemEntities = cartItemDao.findAllInIds(ids);
        if (cartItemEntities.size() != ids.size()) {
            throw new CartItemNotFoundException("ID 목록에 존재하지 않는 장바구니 상품이 있습니다.");
        }

        List<CartItem> cartItems = cartItemEntities.stream()
                .map(CartItemMapper::toDomain)
                .collect(Collectors.toList());
        return new CartItems(cartItems);
    }

    public CartItems findByMemberId(Long memberId) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId)
                .stream()
                .map(CartItemMapper::toDomain)
                .collect(Collectors.toList());
        return new CartItems(cartItems);
    }

    public Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId) {
        return cartItemDao.findByMemberIdAndProductId(memberId, productId).map(CartItemMapper::toDomain);
    }

    public CartItem findById(Long id) {
        return cartItemDao.findById(id)
                .map(CartItemMapper::toDomain)
                .orElseThrow(() -> new CartItemNotFoundException(id));
    }

    public Long save(CartItem cartItem) {
        return cartItemDao.save(CartItemMapper.toEntity(cartItem));
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemMapper.toEntity(cartItem));
    }

    public void deleteAll(CartItems cartItems) {
        cartItemDao.deleteAllInIds(cartItems.extractAllIds());
    }

    public void deleteAllByProductId(Long productId) {
        cartItemDao.deleteAllByProductId(productId);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }
}
