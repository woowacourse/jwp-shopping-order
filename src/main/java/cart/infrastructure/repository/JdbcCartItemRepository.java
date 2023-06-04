package cart.infrastructure.repository;

import static java.util.stream.Collectors.toList;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.repository.CartItemRepository;
import cart.infrastructure.dao.CartItemDao;
import cart.infrastructure.entity.CartItemEntity;
import cart.infrastructure.entity.MemberEntity;
import cart.infrastructure.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public JdbcCartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public List<CartItem> findAllByMemberId(Long id) {
        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(id);
        return cartItemEntities.stream()
                .map(CartItemEntity::toDomain)
                .collect(toList());
    }

    @Override
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
                product.getPrice().getValue(),
                product.getImageUrl()
        );
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemDao.findById(id)
                .map(CartItemEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
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
