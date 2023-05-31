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
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;
    private final ProductDao productDao;

    public CartItemRepository(final CartItemDao cartItemDao, final MemberDao memberDao, final ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
        this.productDao = productDao;
    }

    public Long save(final CartItem cartItem) {
        final CartItemEntity cartItemEntity = cartItemDao.save(new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getMember().getId(),
                cartItem.getProduct().getId()
        ));
        return cartItemEntity.getId();
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        final List<CartItem> cartItems = new ArrayList<>();

        final List<CartItemEntity> cartItemEntities = cartItemDao.findAllByMemberId(memberId);
        for (final CartItemEntity cartItemEntity : cartItemEntities) {
            final CartItem cartItem = new CartItem(
                    cartItemEntity.getId(),
                    cartItemEntity.getQuantity(),
                    findMember(cartItemEntity),
                    findProduct(cartItemEntity)
            );
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    public CartItem findById(final Long id) {
        final CartItemEntity cartItemEntity = cartItemDao.findById(id);
        return new CartItem(cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                findMember(cartItemEntity),
                findProduct(cartItemEntity));
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(
                new CartItemEntity(
                        cartItem.getId(),
                        cartItem.getQuantity(),
                        cartItem.getMember().getId(),
                        cartItem.getProduct().getId()
                )
        );
    }

    private Member findMember(final CartItemEntity cartItemEntity) {
        final MemberEntity memberEntity = memberDao.findById(cartItemEntity.getMemberId());
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword(),
                memberEntity.getTotalPurchaseAmount());
    }

    private Product findProduct(final CartItemEntity cartItemEntity) {
        final ProductEntity productEntity = productDao.findById(cartItemEntity.getProductId());
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(),
                productEntity.getImageUrl(), productEntity.isDiscounted(), productEntity.getDiscountRate());
    }
}
