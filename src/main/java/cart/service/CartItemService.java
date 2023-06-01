package cart.service;

import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.domain.product.CartItem;
import cart.repository.ProductRepository;
import cart.service.request.CartItemQuantityUpdateRequest;
import cart.service.request.CartItemRequest;
import cart.service.response.CartItemResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Optional<CartItem> cartItemOption = cartItemDao.findByMemberIdAndProductId(member.getId(),
                cartItemRequest.getProductId());
        if (cartItemOption.isPresent()) {
            return plusQuantity(member, cartItemRequest, cartItemOption);
        }
        final CartItem cartItem = new CartItem(cartItemRequest.getQuantity(), member,
                productRepository.findById(cartItemRequest.getProductId()));
        return cartItemDao.save(cartItem);
    }

    private Long plusQuantity(final Member member, final CartItemRequest cartItemRequest,
                              final Optional<CartItem> cartItemOption) {
        final CartItem cartItem = cartItemOption.orElseThrow();
        cartItem.checkOwner(member);
        cartItem.changeQuantity(cartItem.getQuantity().getValue() + cartItemRequest.getQuantity());
        cartItemDao.updateQuantity(cartItem);
        return cartItem.getId();
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
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
