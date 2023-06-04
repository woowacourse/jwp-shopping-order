package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dao.dto.PageInfo;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CartItemRepositoryImpl implements CartItemRepository {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;

    public CartItemRepositoryImpl(final ProductDao productDao, final CartItemDao cartItemDao,
                                  final MemberDao memberDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
    }

    public CartItems findByMember(Member member) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());
        return createCartItems(member, cartItemEntities);
    }

    private CartItems createCartItems(final Member member, final List<CartItemEntity> cartItemEntities) {
        return new CartItems(cartItemEntities.stream()
                .map(cartItemEntity -> createCartItem(member, cartItemEntity))
                .collect(Collectors.toList()));
    }

    private CartItem createCartItem(final Member member, final CartItemEntity cartItemEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                convertProductEntityToProduct(cartItemEntity.getProductId()),
                member);
    }

    private Product convertProductEntityToProduct(final Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    public Long add(Member member, Long productId) {
        if (cartItemDao.existsByProductIdAndMemberId(productId, member.getId())) {
            return updateCartItemQuantity(member, productId);
        }

        Product product = convertProductEntityToProduct(productId);
        return cartItemDao.save(new CartItem(member, product));
    }

    private Long updateCartItemQuantity(final Member member, final Long productId) {
        CartItemEntity cartItemEntity = getCartItem(member, productId);
        CartItem cartItem = createCartItem(member, cartItemEntity);
        cartItem.changeQuantity(cartItemEntity.getQuantity() + 1);
        cartItemDao.updateQuantity(cartItem);
        return cartItem.getId();
    }

    private CartItemEntity getCartItem(final Member member, final Long productId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());
        return cartItemEntities.stream()
                .filter(cartItemEntity -> cartItemEntity.equalsProductId(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 productId를 가진 장바구니 상품이 존재하지 않습니다."));
    }

    public void updateQuantity(CartItem cartItem) {
        if (cartItem.getQuantity() == 0) {
            cartItemDao.deleteById(cartItem.getId());
            return;
        }

        cartItemDao.updateQuantity(cartItem);
    }

    public CartItem findById(final Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id);
        Member member = memberDao.getMemberById(cartItemEntity.getMemberId());
        return createCartItem(member, cartItemEntity);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    public CartItems findCartItemsByPage(Member member, int size, int page) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findCartItemsByPage(member.getId(), size, page);
        return createCartItems(member, cartItemEntities);
    }

    public PageInfo findPageInfo(Member member, int size, int page) {
        return cartItemDao.findPageInfo(member.getId(), size, page);
    }
}
