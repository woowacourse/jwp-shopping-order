package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    @Transactional
    public Long create(Member member, CartItemRequest cartItemRequest) {
        final ProductEntity productEntity = productDao.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 상품이 없습니다."));
        final CartItem cartItem = new CartItem(member, ProductEntity.toDomain(productEntity));
        return cartItemDao.create(CartItemEntity.from(cartItem));
    }

    public List<CartItemResponse> findByMember(Member member) {

        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId())
                .stream()
                .map(CartItemEntity::toDomain)
                .collect(Collectors.toUnmodifiableList());

        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 장바구니 아이템이 없습니다."));
        final CartItem cartItem = CartItemEntity.toDomain(cartItemEntity);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(CartItemEntity.from(cartItem));
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItemEntity cartItemEntity = cartItemDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 장바구니 아이템이 없습니다."));
        final CartItem cartItem = CartItemEntity.toDomain(cartItemEntity);
        cartItem.checkOwner(member);
        cartItemDao.deleteById(id);
    }
}
