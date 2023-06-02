package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import cart.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public CartItem findById(final Long id) {
        return toDomain(cartItemDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 상품이 없습니다.")));
    }

    public List<CartItem> findByIds(final List<Long> ids) {
        return ids
                .stream()
                .map(id -> cartItemDao.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("해당하는 장바구니 아이템이 없습니다.")))
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        return cartItemDao.findByMemberId(memberId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long save(final CartItem cartItem) {
        return cartItemDao.save(toEntity(cartItem));
    }

    public void update(final CartItem cartItem) {
        cartItemDao.update(toEntity(cartItem));
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    private CartItem toDomain(final CartItemEntity cartItemEntity) {
        return new CartItem(
                cartItemEntity.getId(),
                cartItemEntity.getQuantity(),
                new Product(
                        cartItemEntity.getProductId(),
                        cartItemEntity.getProductName(),
                        cartItemEntity.getProductPrice(),
                        cartItemEntity.getProductImageUrl())
                ,
                new Member(
                        cartItemEntity.getMemberId(),
                        cartItemEntity.getMemberEmail(),
                        cartItemEntity.getMemberPassword(),
                        cartItemEntity.getMemberPoint()
                )
        );
    }

    private CartItemEntity toEntity(final CartItem cartItem) {
        return new CartItemEntity(
                cartItem.getId(),
                cartItem.getMember().getId(),
                cartItem.getMember().getEmail(),
                cartItem.getMember().getPassword(),
                cartItem.getMember().getPoint(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getQuantity()
        );
    }
}
