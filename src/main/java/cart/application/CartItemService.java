package cart.application;

import cart.domain.cartItem.CartItem;
import cart.domain.cartItem.CartItemRepository;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cartItem.CartItemQuantityUpdateRequest;
import cart.dto.cartItem.CartItemRequest;
import cart.dto.cartItem.CartItemResponse;
import cart.exception.NoSuchProductException;
import cart.persistence.dao.ProductDao;
import cart.util.ModelSortHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductDao productDao, CartItemRepository cartItemRepository) {
        this.productDao = productDao;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findCartItemsByMemberId(member.getId());
        ModelSortHelper.sortByIdInDescending(cartItems);
        return cartItems.stream().map(CartItemResponse::from).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productDao.findProductById(cartItemRequest.getProductId()).orElseThrow(() -> new NoSuchProductException());
        return cartItemRepository.add(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
