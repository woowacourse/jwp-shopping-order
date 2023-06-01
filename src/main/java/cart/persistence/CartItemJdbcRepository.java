package cart.persistence;

import cart.application.repository.CartItemRepository;
import cart.domain.Member;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dto.CartDetailDTO;
import cart.persistence.entity.CartItemEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemJdbcRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public long create(final CartItem cartItem) {
        CartItemEntity entity = CartItemEntity.from(cartItem);
        return cartItemDao.create(entity);
    }

    @Override
    public List<CartItem> findByMember(final Member member) {
        List<CartDetailDTO> cartDetails = cartItemDao.findByMemberId(member.getId());
        return cartDetails.stream()
                .map(CartDetailDTO::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartItem> findById(final long id) {
        Optional<CartDetailDTO> optionalCartDetail = cartItemDao.findById(id);
        if (optionalCartDetail.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(optionalCartDetail.get().toDomain());
    }

    @Override
    public CartItems findByIds(final List<Long> ids) {
        List<CartDetailDTO> cartDetails = cartItemDao.findByIds(ids);
        List<CartItem> cartItems = cartDetails.stream()
                .map(CartDetailDTO::toDomain)
                .collect(Collectors.toList());
        return new CartItems(cartItems);
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemEntity.from(cartItem));
    }

    @Override
    public void deleteById(final long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void deleteAll(final CartItems cartItems) {
        List<Long> cartItemIds = cartItems.getCartItems().stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        cartItemIds.forEach(id -> {
            if (id == null) {
                throw new IllegalArgumentException("카트 아이템의 id가 없습니다.");
            }
        });

        cartItemDao.deleteByIds(cartItemIds);
    }
}
