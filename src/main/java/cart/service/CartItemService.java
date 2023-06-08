package cart.service;

import cart.controller.request.CartItemQuantityUpdateRequestDto;
import cart.controller.request.CartItemRequestDto;
import cart.controller.response.CartItemResponseDto;
import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private static final Integer DEFAULT_QUANTITY = 1;

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponseDto> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponseDto::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequestDto cartItemRequest) {
        Integer quantity = cartItemRequest.getQuantity()
                .orElseGet(() -> DEFAULT_QUANTITY);

        return cartItemDao.save(
                new CartItem(quantity, member, productDao.getProductById(cartItemRequest.getProductId()))
        );
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequestDto request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

}
