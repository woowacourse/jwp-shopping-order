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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Optional<CartItem> cartItemOption = cartItemDao.findByMemberIdAndProductId(member.getId(),
                cartItemRequest.getProductId());
        return cartItemOption
                .map(cartItem -> plusQuantity(member, cartItemRequest, cartItem))
                .orElseGet(() -> createNewCartItem(member, cartItemRequest));
    }

    private Long plusQuantity(final Member member, final CartItemRequest cartItemRequest,
                              final CartItem cartItem) {
        final int newQuantity = cartItem.getQuantity().getValue() + cartItemRequest.getQuantity();
        updateQuantity(member, cartItem, newQuantity);
        return cartItem.getId();
    }

    private void updateQuantity(final Member member, final CartItem cartItem, final int quantity) {
        cartItem.checkOwner(member);
        if (quantity == 0) {
            cartItemDao.deleteById(cartItem.getId());
            return;
        }
        cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(cartItem);
    }

    private Long createNewCartItem(final Member member, final CartItemRequest cartItemRequest) {
        final CartItem cartItem = cartItemDao.save(new CartItem(cartItemRequest.getQuantity(), member,
                productRepository.findById(cartItemRequest.getProductId())));
        return cartItemDao.save(cartItem).getId();
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id);
        final int quantity = request.getQuantity();
        updateQuantity(member, cartItem, quantity);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
