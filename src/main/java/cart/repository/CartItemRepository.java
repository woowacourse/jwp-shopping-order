package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
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

    public List<CartItem> findByMemberId(Long id) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(id);
        return cartItemEntities.stream()
                .map(CartItemEntity::toDomain)
                .collect(toList());
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

    private MemberEntity toEntity(Member member) {
        return new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword()
        );
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public Optional<CartItem> findById(Long id) {
        return cartItemDao.findById(id)
                .map(CartItemEntity::toDomain);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
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
}
