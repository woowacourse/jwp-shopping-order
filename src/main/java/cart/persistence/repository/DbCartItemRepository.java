package cart.persistence.repository;

import cart.domain.cartItem.CartItem;
import cart.exception.NoSuchCartItemException;
import cart.exception.NoSuchMemberException;
import cart.exception.NoSuchProductException;
import cart.persistence.dao.CartItemDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbCartItemRepository implements cart.domain.cartItem.CartItemRepository {
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final MemberDao memberDao;

    public DbCartItemRepository(CartItemDao cartItemDao, ProductDao productDao, MemberDao memberDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.memberDao = memberDao;
    }

    @Override
    public List<CartItem> findCartItemsByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findCartItemsByMemberId(memberId);
        return cartItemEntities.stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toList());
    }

    @Override
    public Long add(CartItem cartItem) {
        return cartItemDao.add(mapToCartItemEntity(cartItem));
    }

    @Override
    public CartItem findById(Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id).orElseThrow(() -> new NoSuchCartItemException());
        return mapToCartItem(cartItemEntity);
    }

    @Override
    public void delete(Long memberId, Long productId) {
        cartItemDao.delete(memberId, productId);
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(mapToCartItemEntity(cartItem));
    }

    private CartItemEntity mapToCartItemEntity(CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getProduct().getId(),
                cartItem.getMember().getId()
        );
    }

    private CartItem mapToCartItem(CartItemEntity cartItemEntity) {
        MemberEntity memberEntity = memberDao.findById(cartItemEntity.getMemberId()).orElseThrow(() -> new NoSuchMemberException());

        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                productDao.findProductById(cartItemEntity.getProductId()).orElseThrow(() -> new NoSuchProductException()), // TODO repository 생성
                MemberMapper.toDomain(memberEntity)
        );
    }
}
