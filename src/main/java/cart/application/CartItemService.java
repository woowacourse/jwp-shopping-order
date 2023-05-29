package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.ProductNotFoundException;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemDao cartItemDao;
    private final ProductRepository productRepository;

    public CartItemService(final CartItemDao cartItemDao, final ProductRepository productRepository) {
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Long productId = cartItemRequest.getProductId();
        final CartItem cartItem = new CartItem(
                member,
                productRepository.getProductById(productId).orElseThrow(() -> new ProductNotFoundException(productId))
        );
        return cartItemDao.save(cartItem);
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
