package cart.repository;

import cart.dao.CartItemDao2;
import cart.dao.ProductDao2;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {
    private final CartItemDao2 cartItemDao;
    private final ProductDao2 productDao;

    public CartItemRepository(final CartItemDao2 cartItemDao, final ProductDao2 productDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public List<CartItem> findByMember(final Member member) {
        Map<Long, ProductEntity> allProductsById = productDao.findAll().stream()
                .collect(Collectors.toMap(ProductEntity::getId, productEntity -> productEntity));

        List<CartItemEntity> cartItemEntities = cartItemDao.findByMemberId(member.getId());

        return cartItemEntities.stream()
                .map(cartItemEntity -> cartItemEntity.toCartItem(allProductsById, member))
                .collect(Collectors.toList());
    }
}
