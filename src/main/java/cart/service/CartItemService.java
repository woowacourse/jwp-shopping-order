package cart.service;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.ProductException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Optional<Product> product = productDao.getProductById(cartItemRequest.getProductId());
        if (product.isPresent()) {
            return cartItemDao.save(new CartItem(member, product.get()));
        }
        throw new ProductException("존재하지 않는 상품입니다");
    }

    public CartItem findById(Long id) {
        Optional<CartItem> cartItem = cartItemDao.findById(id);
        if (cartItem.isPresent()) {
            return cartItem.get();
        }
        throw new CartItemException("장바구니 목록에서 조회할 수 없습니다");
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
