package cart.service;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.product.CartItem;
import cart.domain.Member;
import cart.service.request.CartItemQuantityUpdateRequest;
import cart.service.request.CartItemRequest;
import cart.service.response.CartItemResponse;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final ProductRepository productRepository;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao,
                           final ProductRepository productRepository) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItem(member, productRepository.findById(cartItemRequest.getProductId())));
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
