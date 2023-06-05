package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Product;
import cart.dto.MemberInfo;
import cart.entity.CartItemEntity;
import cart.entity.MemberInfoEntity;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public CartItem save(CartItem cartItem) {
        Long id = cartItemDao.save(
                new CartItemEntity(
                        cartItem.getId(),
                        toEntity(cartItem.getProduct()),
                        toEntity(cartItem.getMember()),
                        cartItem.getQuantity()
                )
        );
        return new CartItem(id, cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
    }

    public Optional<CartItem> findById(Long id) {
        return cartItemDao.findById(id)
                .map(CartItemEntity::toDomain);
    }

    public List<CartItem> findAllByMemberId(Long id) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findAllByMemberId(id);
        return cartItemEntities.stream()
                .map(CartItemEntity::toDomain)
                .collect(toList());
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(
                new CartItemEntity(
                        cartItem.getId(),
                        toEntity(cartItem.getProduct()),
                        toEntity(cartItem.getMember()),
                        cartItem.getQuantity()
                )
        );
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice().getValue(),
                product.getImageUrl()
        );
    }

    private MemberInfoEntity toEntity(MemberInfo member) {
        return new MemberInfoEntity(
                member.getId(),
                member.getEmail()
        );
    }
}
