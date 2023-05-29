package cart.persistence;

import cart.application.mapper.CartItemMapper;
import cart.application.mapper.MemberMapper;
import cart.application.mapper.ProductMapper;
import cart.application.repository.CartItemRepository;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.MemberNotFoundException;
import cart.exception.ProductNotFoundException;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
import java.util.List;
import java.util.Map;
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
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());
        List<Long> productIds = cartItemEntities.stream()
                .map(CartItemEntity::getProductId)
                .collect(Collectors.toList());
        Map<Long, Product> products = productDao.findByIds(productIds).stream()
                .map(ProductMapper::toProduct)
                .collect(Collectors.toMap(Product::getId, product -> product));
        return cartItemEntities.stream()
                .map(it -> new CartItem(it.getId(), it.getQuantity(), products.get(it.getProductId()), member))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CartItem> findById(final long id) {
        Optional<CartItemEntity> optionalCartItemEntity = cartItemDao.findById(id);
        if (optionalCartItemEntity.isEmpty()) {
            return Optional.empty();
        }
        CartItemEntity cartItemEntity = optionalCartItemEntity.get();

        MemberEntity memberEntity = memberDao.findMemberById(cartItemEntity.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        ProductEntity productEntity = productDao.findById(cartItemEntity.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        return Optional.of(
                new CartItem(id, cartItemEntity.getQuantity(), ProductMapper.toProduct(productEntity),
                        MemberMapper.toMember(memberEntity)));
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
