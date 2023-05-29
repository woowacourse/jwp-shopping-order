package cart.repository;

import static cart.exception.ErrorMessage.NOT_FOUND_CART_ITEM;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemException;
import cart.repository.dao.CartItemDao;
import cart.repository.entity.CartItemEntity;
import cart.repository.entity.CartItemWithMemberAndProductEntity;
import cart.repository.entity.MemberEntity;
import cart.repository.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = toEntity(cartItem);

        return cartItemDao.save(cartItemEntity);
    }

    public List<CartItem> findByMemberId(Member member) {
        List<CartItemWithMemberAndProductEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());

        return cartItemEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public CartItem findById(Long id) {
        CartItemWithMemberAndProductEntity cartItemWithMemberAndProductEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemException(NOT_FOUND_CART_ITEM));

        return toDomain(cartItemWithMemberAndProductEntity);
    }

    public void updateQuantity(CartItem cartItem) {
        CartItemEntity cartItemEntity = toEntity(cartItem);

        int updatedRow = cartItemDao.updateQuantity(cartItemEntity);
        if (updatedRow == 0) {
            throw new CartItemException(NOT_FOUND_CART_ITEM);
        }
    }

    public void deleteById(Long id) {
        int deletedRow = cartItemDao.deleteById(id);

        if (deletedRow == 0) {
            throw new CartItemException(NOT_FOUND_CART_ITEM);
        }
    }

    private CartItemEntity toEntity(CartItem cartItem) {
        Member member = cartItem.getMember();
        Product product = cartItem.getProduct();

        return new CartItemEntity(null,
                member.getId(),
                product.getId(),
                cartItem.getQuantity(),
                null, null
        );
    }

    private CartItem toDomain(CartItemWithMemberAndProductEntity cartItemEntity) {
        MemberEntity memberEntity = cartItemEntity.getMemberEntity();
        Member member = new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );

        ProductEntity productEntity = cartItemEntity.getProductEntity();

        Product product = new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl()
        );

        CartItemEntity cartItem = cartItemEntity.getCartItemEntity();
        return new CartItem(cartItem.getId(), cartItem.getQuantity(), product, member);
    }
}
