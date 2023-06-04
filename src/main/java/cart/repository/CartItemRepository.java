package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao2;
import cart.dao.ProductDao2;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final ProductDao2 productDao;
    private final MemberDao2 memberDao2;

    public CartItemRepository(final CartItemDao cartItemDao, final ProductDao2 productDao, final MemberDao2 memberDao2) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao2 = memberDao2;
    }

    public Long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = CartItemEntity.from(cartItem);
        return cartItemDao.save(cartItemEntity);
    }

    public List<CartItem> findByMember(final Member member) {
        Map<Long, Product> allProductsById = productDao.findAll().stream()
                .collect(Collectors.toMap(ProductEntity::getId, ProductEntity::toProduct));

        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());

        return cartItemEntities.stream()
                .map(cartItemEntity -> cartItemEntity.toCartItem(
                        allProductsById.get(cartItemEntity.getProductId()),
                        member))
                .collect(Collectors.toList());
    }

    public CartItem findById(final Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니는 없습니다."));
        ProductEntity productEntity = productDao.findById(cartItemEntity.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 없습니다."));
        MemberEntity memberEntity = memberDao2.findById(cartItemEntity.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버는 없습니다."));

        Product product = productEntity.toProduct();
        Member member = memberEntity.toMember();
        return cartItemEntity.toCartItem(product, member);
    }

    public List<CartItem> findByIds(final List<Long> cartItemsIds) {
        List<CartItemEntity> cartItems = cartItemDao.findByIds(cartItemsIds);
        Map<Long, MemberEntity> allMembersById = memberDao2.findAll().stream()
                .collect(Collectors.toMap(MemberEntity::getId, memberEntity -> memberEntity));
        Map<Long, Product> allProductsById = productDao.findAll().stream()
                .collect(Collectors.toMap(ProductEntity::getId, ProductEntity::toProduct));
        return cartItems.stream()
                .map(cartItemEntity -> {
                    Member member = allMembersById.get(cartItemEntity.getMemberId()).toMember();
                    Product product = allProductsById.get(cartItemEntity.getProductId());
                    return cartItemEntity.toCartItem(product, member);
                })
                .collect(Collectors.toList());
    }

    public void update(CartItem cartItem) {
        CartItemEntity cartItemEntity = CartItemEntity.from(cartItem);
        cartItemDao.update(cartItemEntity);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public void deleteAll(final List<CartItem> cartItems) {
        List<Long> ids = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        cartItemDao.deleteAllByIds(ids);
    }
}
