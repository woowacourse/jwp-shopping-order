package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.dto.CartItemProductDto;
import cart.dao.entity.CartItemEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.IllegalCartException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = CartItemEntity.fromDomain(cartItem);
        return cartItemDao.save(cartItemEntity);
    }

    public CartItem findById(long cartItemId) {
        CartItemProductDto foundCartItem = cartItemDao.findById(cartItemId)
            .orElseThrow(() -> new IllegalCartException("존재하지 않는 장바구니 id 입니다."));

        return foundCartItem.toDomain();
    }

    public List<CartItem> findByMember(Member member) {
        return cartItemDao.findByMemberId(member.getId())
            .stream()
            .map(CartItemProductDto::toDomain)
            .collect(Collectors.toList());
    }

    public void delete(CartItem cartItem) {
        validateCartItemExistence(cartItem.getId());
        cartItemDao.deleteById(cartItem.getId());
    }

    public void updateQuantity(CartItem cartItem) {
        validateCartItemExistence(cartItem.getId());
        cartItemDao.updateQuantity(CartItemEntity.fromDomain(cartItem));
    }

    private void validateCartItemExistence(long cartItemId) {
        if (cartItemDao.isNonExistingId(cartItemId)) {
            throw new IllegalCartException("존재하지 않는 장바구니 id 입니다.");
        }
    }

    public List<CartItem> findAllByIds(List<Long> ids) {
        // TODO: 2023-06-01 조회한 결과 갯수 != ids 크기이면 존재하지 않는 CartId 요청으로 예외?
        return cartItemDao.findAllByIds(ids)
            .stream()
            .map(CartItemProductDto::toDomain)
            .collect(Collectors.toList());
    }

    public void removeAllByIds(List<Long> ids) {
        cartItemDao.removeAllByIds(ids);
    }
}
