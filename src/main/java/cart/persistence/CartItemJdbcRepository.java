package cart.persistence;

import cart.application.mapper.CartItemMapper;
import cart.application.repository.CartItemRepository;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.ProductDao;
import cart.persistence.dto.CartDetailDTO;
import cart.persistence.entity.CartItemEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;

    public CartItemJdbcRepository(final CartItemDao cartItemDao, final MemberDao memberDao,
            final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }

    @Override
    public long create(final CartItem cartItem) {
        CartItemEntity entity = CartItemMapper.toEntity(cartItem);
        return cartItemDao.create(entity);
    }

    @Override
    public List<CartItem> findByMember(final Member member) {
        List<CartDetailDTO> cartDetails = cartItemDao.findByMemberId(member.getId());
        return cartDetails.stream()
                .map(CartItemMapper::toCartItem)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartItem> findById(final long id) {
        Optional<CartDetailDTO> optionalCartDetail = cartItemDao.findById(id);
        if (optionalCartDetail.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(CartItemMapper.toCartItem(optionalCartDetail.get()));
    }

    @Override
    public void deleteById(final long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemMapper.toEntity(cartItem));
    }
}
