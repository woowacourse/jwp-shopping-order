package cart.cartitem.repository;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.member.repository.MemberEntity;
import cart.product.dao.ProductDao;
import cart.product.domain.Product;
import cart.product.repository.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;
    
    public CartItemRepository(final CartItemDao cartItemDao, final ProductDao productDao, final MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }
    
    public List<CartItem> findByMemberId(final Long memberId) {
        final List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);
        return cartItemEntities.stream()
                .map(cartItemEntity -> toCartItemWithMemberId(memberId, cartItemEntity))
                .collect(Collectors.toUnmodifiableList());
    }
    
    private CartItem toCartItemWithMemberId(final Long memberId, final CartItemEntity cartItemEntity) {
        final Long productId = cartItemEntity.getProductId();
        final ProductEntity productEntity = productDao.getProductById(productId);
        final MemberEntity memberEntity = memberDao.getMemberById(memberId);
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                Product.from(productEntity),
                Member.from(memberEntity)
        );
    }
    
    public List<CartItem> findAllByIds(final List<Long> cartItemIds) {
        return cartItemDao.findByIds(cartItemIds).stream()
                .map(this::toCartItem)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public CartItem findById(final Long id) {
        return toCartItem(cartItemDao.findById(id));
    }
    
    private CartItem toCartItem(final CartItemEntity cartItemEntity) {
        final ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());
        final MemberEntity memberEntity = memberDao.getMemberById(cartItemEntity.getMemberId());
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                Product.from(productEntity),
                Member.from(memberEntity)
        );
    }
    
    public Long save(final CartItem cartItem) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(cartItem.getMember().getEmail());
        final CartItemEntity cartItemEntity = new CartItemEntity(null, memberEntity.getId(), cartItem.getProduct().getId(), cartItem.getQuantity());
        return cartItemDao.save(cartItemEntity);
    }
    
    public void removeById(final Long id) {
        cartItemDao.deleteById(id);
    }
    
    public void update(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
        cartItemDao.update(cartItemEntity);
    }
}
