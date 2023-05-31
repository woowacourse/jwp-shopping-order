package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.entity.CartItemDto;
import cart.domain.CartItem;
import cart.domain.CartItems;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemJdbcRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public CartItems findAllByCartItemIds(final List<Long> itemIds) {
        List<CartItemDto> cartItemEntities = cartItemDao.findByIds(itemIds);
        final List<CartItem> cartItems = cartItemEntities.stream()
                .map(CartItemDto::toDomain)
                .collect(toList());
        return CartItems.from(cartItems);
    }

    @Override
    public void deleteAllByIds(final List<Long> cartItemIds) {
        cartItemDao.deleteAllByIds(cartItemIds);
    }
}
