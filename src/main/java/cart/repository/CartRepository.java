package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import cart.exception.CartItemException;
import cart.exception.MemberException;
import cart.exception.ProductException;
import cart.repository.mapper.CartItemMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public CartRepository(
            final CartItemDao cartItemDao,
            final ProductDao productDao,
            final MemberDao memberDao
    ) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    public Cart findByMember(final Member member) {
        final List<CartItemEntity> cartItems = cartItemDao.findByMemberId(member.getId());

        final MemberEntity memberEntity = memberDao.getMemberById(member.getId())
                .orElseThrow(() -> new MemberException.NotFound(member.getId()));

        final List<Long> productIds = cartItems.stream()
                .map(CartItemEntity::getProductId)
                .collect(Collectors.toUnmodifiableList());

        Map<Long, ProductEntity> productInCart = productDao.getProductGroupById(productIds);

        return CartItemMapper.toCart(cartItems, productInCart, memberEntity);
    }

    public Long save(final CartItem cartItem) {
        return cartItemDao.save(CartItemMapper.toEntity(cartItem));
    }

    public CartItem findById(final Long id) {
        final CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemException.NotFound(id));

        final ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId())
                .orElseThrow(() -> new ProductException.NotFoundProduct(cartItemEntity.getProductId()));

        final MemberEntity memberEntity = memberDao.getMemberById(cartItemEntity.getMemberId())
                .orElseThrow(() -> new MemberException.NotFound(cartItemEntity.getMemberId()));

        return CartItemMapper.toCartItem(cartItemEntity, productEntity, memberEntity);
    }

    public Cart findByIds(final List<Long> cartItemIds) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(cartItemIds);
        final Map<Long, ProductEntity> productGroupById = getProductGroupById(cartItemEntities);
        final Map<Long, MemberEntity> memberGroupByMemberId = getMemberGroupByMemberId(cartItemEntities);

        return new Cart(cartItemEntities.stream().map(it -> CartItemMapper.toCartItem(
                it,
                productGroupById.get(it.getId()),
                memberGroupByMemberId.get(it.getMemberId())
        )).collect(Collectors.toUnmodifiableList())
        );
    }

    private Map<Long, ProductEntity> getProductGroupById(final List<CartItemEntity> cartItemEntities) {
        return productDao.getProductGroupById(getAllProductId(cartItemEntities));
    }

    private List<Long> getAllProductId(final List<CartItemEntity> cartItemEntities) {
        return cartItemEntities.stream()
                .map(CartItemEntity::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    private Map<Long, MemberEntity> getMemberGroupByMemberId(final List<CartItemEntity> cartItemEntities) {
        final List<Long> memberIds = cartItemEntities.stream()
                .map(CartItemEntity::getMemberId)
                .collect(Collectors.toUnmodifiableList());

        return memberDao.findMemberGroupById(memberIds);
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemMapper.toEntity(cartItem));
    }

    public void deleteCart(final Cart cart) {
        final List<Long> cartItemIds = getAllCartItemIds(cart);
        cartItemDao.deleteByIds(cartItemIds);
    }

    private List<Long> getAllCartItemIds(final Cart cart) {
        return cart.getCartItems().stream()
                .map(CartItem::getId)
                .collect(Collectors.toUnmodifiableList());
    }
}
