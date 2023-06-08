package cart.Repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import cart.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cart.Repository.mapper.CartItemMapper.*;

@Repository
public class CartItemRepository {
    private final ProductDao productDao;
    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;

    public CartItemRepository(ProductDao productDao, MemberDao memberDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }

    public Cart findByMemberId(Long memberId) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(memberId);

        if (cartItemEntities.isEmpty()) {
            return new Cart(Collections.emptyList());
        }

        List<Long> memberIds = toMemberIds(cartItemEntities);

        Map<Long, MemberEntity> memberEntityById = memberDao.getMemberByIds(memberIds);
        Map<Long, ProductEntity> productEntityById = getProductInCarts(cartItemEntities);

        return toCart(
                cartItemEntities,
                productEntityById,
                memberEntityById);
    }

    private static List<Long> toMemberIds(List<CartItemEntity> cartItemEntities) {
        return cartItemEntities.stream()
                .map(CartItemEntity::getMemberId)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    private Map<Long, ProductEntity> getProductInCarts(List<CartItemEntity> cartItemEntities) {
        List<Long> cartItemIds = cartItemEntities.stream().map(CartItemEntity::getProductId)
                .collect(Collectors.toUnmodifiableList());

        return productDao.getProductByIds(cartItemIds);
    }

    public Cart findByIds(List<Long> cartItemIds) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByIds(cartItemIds);

        if (cartItemEntities.isEmpty()) {
            return new Cart(Collections.emptyList());
        }
        Map<Long, ProductEntity> productEntityById = getProductInCarts(cartItemEntities);

        List<Long> memberIds = toMemberIds(cartItemEntities);

        Map<Long, MemberEntity> memberEntityByIds = memberDao.getMemberByIds(memberIds);

        return toCart(
                cartItemEntities,
                productEntityById,
                memberEntityByIds);
    }

    public Long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = toCartItemEntity(cartItem);
        return cartItemDao.save(cartItemEntity);
    }

    public CartItem findById(Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new NotFoundException.CartItem(id));

        ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId())
                .orElseThrow(() -> new NotFoundException.Product(cartItemEntity.getProductId()));

        MemberEntity memberEntity = memberDao.getMemberById(cartItemEntity.getMemberId())
                .orElseThrow(() -> new NotFoundException.Member(cartItemEntity.getMemberId()));

        return toCartItem(cartItemEntity, productEntity, memberEntity);
    }

    public void updateQuantity(CartItem cartItem) {
        CartItemEntity cartItemEntity = toCartItemEntity(cartItem);
        try {
            cartItemDao.updateQuantity(cartItemEntity);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException.CartItem(cartItem.getId());
        }
    }

    public void deleteById(Long id) {
        try {
            cartItemDao.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException.CartItem(id);
        }
    }

    public void deleteByIds(List<Long> cartItemIds) {
        try {
            cartItemDao.deleteByIds(cartItemIds);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException.CartItem(cartItemIds);
        }
    }
}
