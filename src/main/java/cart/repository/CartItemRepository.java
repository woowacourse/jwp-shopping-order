package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import cart.exception.NoSuchMemberException;
import cart.exception.NoSuchProductException;
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

    public long save(final CartItem cartItem) {
        final CartItemEntity entity = CartItemEntity.from(cartItem);
        return cartItemDao.save(entity);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final List<CartItemEntity> entity = cartItemDao.findByMemberId(memberId);
        return entity.stream()
                .map(it -> new CartItem(
                        it.getId(),
                        it.getQuantity(),
                        productDao.getProductById(it.getProductId()).toProduct(),
                        memberDao.getMemberById(it.getMemberId()).toMember()
                ))
                .collect(Collectors.toList());
    }

    public long findProductIdOf(final Long id) {
        return cartItemDao.findById(id).getProductId();
    }

    public int findQuantityOf(final Long id) {
        return cartItemDao.findById(id).getQuantity();
    }

    public CartItem findById(final Long id) {
        final CartItemEntity entity = cartItemDao.findById(id);

        final Member member = findMember(entity.getMemberId());
        final Product product = findProduct(entity.getProductId());

        return new CartItem(id, entity.getQuantity(), product, member);
    }

    private Product findProduct(final Long productId) {
        final ProductEntity productEntity = productDao.getProductById(productId);
        if (productEntity == null) {
            throw new NoSuchProductException("해당 상품을 찾을 수 없습니다");
        }
        return productEntity.toProduct();
    }

    private Member findMember(final Long memberId) {
        final MemberEntity memberEntity = memberDao.getMemberById(memberId);
        if (memberEntity == null) {
            throw new NoSuchMemberException("해당 멤버를 찾을 수 없습니다");
        }
        return memberEntity.toMember();
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(CartItemEntity.from(cartItem));
    }
}
