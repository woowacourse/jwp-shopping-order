package cart.Repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Repository
public class CartItemRepository {
    private final CartItemMapper cartItemMapper;
    private final ProductDao productDao;
    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemMapper cartItemMapper, ProductDao productDao, MemberDao memberDao, CartItemDao cartItemDao) {
        this.cartItemMapper = cartItemMapper;
        this.productDao = productDao;
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);

        Map<Long, ProductEntity> productsInCart = getProductInCarts(cartItemEntities);

        MemberEntity memberEntity = memberDao.getMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 회원이 없습니다."));

        return cartItemMapper.toCartItems(cartItemEntities, productsInCart, memberEntity);
    }

    private Map<Long, ProductEntity> getProductInCarts(List<CartItemEntity> cartItemEntities) {
        List<Long> cartItemIds = cartItemEntities.stream().map(CartItemEntity::getProductId)
                .collect(Collectors.toUnmodifiableList());

        Map<Long, ProductEntity> productsInCart = productDao.getProductByIds(cartItemIds)
                .stream()
                .collect(toMap(ProductEntity::getId, productEntity -> productEntity));
        return productsInCart;
    }

    public List<CartItem> findByIds(List<Long> cartItemIds) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(cartItemIds);
        Map<Long, ProductEntity> productsInCarts = getProductInCarts(cartItemEntities);

        MemberEntity memberEntity = memberDao.getMemberById(cartItemEntities.get(0).getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 회원이 없습니다."));

        return cartItemMapper.toCartItems(cartItemEntities, productsInCarts, memberEntity);
    }

    public Long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = cartItemMapper.toCartItemEntity(cartItem);
        return cartItemDao.save(cartItemEntity);
    }

    public CartItem findById(Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 장바구니가 없습니다."));

        ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 상품이 없습니다."));

        MemberEntity memberEntity = memberDao.getMemberById(cartItemEntity.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 회원이 없습니다."));

        return cartItemMapper.toCartItem(cartItemEntity, productEntity, memberEntity);
    }

    public void updateQuantity(CartItem cartItem) {
        CartItemEntity cartItemEntity = cartItemMapper.toCartItemEntity(cartItem);
        cartItemDao.updateQuantity(cartItemEntity);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void deleteByIds(List<Long> cartItemIds) {
        cartItemDao.deleteByIds(cartItemIds);
    }
}
