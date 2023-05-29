package cart.application.service;

import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.exception.ProductNotFoundException;
import cart.infrastructure.repository.dao.CartItemDao;
import cart.infrastructure.repository.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return cartItemDao.save(new CartItem(member, product));
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
