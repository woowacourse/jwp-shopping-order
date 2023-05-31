package cart.repository;

import static cart.exception.CartItemException.DuplicateIds;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.cartitem.CartItem;
import cart.exception.CartItemException.NotFound;
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

    public List<CartItem> findAllInIds(List<Long> ids) {
        Set<Long> uniqueIds = new HashSet<>(ids);
        if (uniqueIds.size() != ids.size()) {
            throw new DuplicateIds();
        }

        List<CartItemEntity> cartItemEntities = cartItemDao.findAllInIds(ids);
        if (cartItemEntities.size() != ids.size()) {
            throw new NotFound();
        }

        return cartItemEntities.stream()
                .map(CartItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<CartItem> findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        return cartItemEntities.stream()
                .map(CartItemMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId) {
        return cartItemDao.findByMemberIdAndProductId(memberId, productId).map(CartItemMapper::toDomain);
    }

    public CartItem findById(Long id) {
        return cartItemDao.findById(id)
                .map(CartItemMapper::toDomain)
                .orElseThrow(NotFound::new);
    }

    public Long save(CartItem cartItem) {
        return cartItemDao.save(CartItemMapper.toEntity(cartItem));
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemMapper.toEntity(cartItem));
    }

    public void deleteAll(List<CartItem> cartItems) {
        cartItemDao.deleteAllInIds(extractIds(cartItems));
    }

    private List<Long> extractIds(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public void deleteAllByProductId(Long productId) {
        cartItemDao.deleteAllByProductId(productId);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }
}
