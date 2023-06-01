package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
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
                .map(cartItemEntity -> {
                    final Long productId = cartItemEntity.getProductId();
                    final ProductEntity productEntity = productDao.getProductById(productId);
                    final MemberEntity memberEntity = memberDao.getMemberById(memberId);
                    return new CartItem(
                            cartItemEntity.getId(),
                            cartItemEntity.getQuantity(),
                            Product.from(productEntity),
                            Member.from(memberEntity)
                    );
                })
                .collect(Collectors.toUnmodifiableList());
    }
    
    public Long save(final CartItem cartItem) {
        final Member memberByEmail = memberDao.getMemberByEmail(cartItem.getMember().getEmail());
        final CartItemEntity cartItemEntity = new CartItemEntity(null, memberByEmail.getId(), cartItem.getProduct().getId(), cartItem.getQuantity());
        return cartItemDao.save(cartItemEntity);
    }
    
    public CartItem findById(final Long id) {
        final CartItemEntity cartItemEntity = cartItemDao.findById(id);
        final ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());
        final MemberEntity memberEntity = memberDao.getMemberById(cartItemEntity.getMemberId());
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                Product.from(productEntity),
                Member.from(memberEntity)
        );
    }
    
    public void removeById(final Long id) {
        cartItemDao.deleteById(id);
    }
    
    public void updateQuantity(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );
        cartItemDao.updateQuantity(cartItemEntity);
    }
}
