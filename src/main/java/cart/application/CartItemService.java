package cart.application;

import cart.dao.CartItemDao;
import cart.domain.cart.CartItem;
import cart.domain.cart.Quantity;
import cart.domain.member.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemDao cartItemDao;

    public CartItemService(final ProductRepository productRepository, final CartItemDao cartItemDao) {
        this.productRepository = productRepository;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItem(member, productRepository.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(new Quantity(request.getQuantity()));
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
