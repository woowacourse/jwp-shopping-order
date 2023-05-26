package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        Product product = productDao.getProductById(cartItemRequest.getProductId());

        if (Objects.isNull(product)) {
            throw new CartItemException.NotFound("카트에 추가하려는 상품이 존재하지 않습니다.");
        }

        return cartItemDao.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);

        if (Objects.isNull(cartItem)) {
            throw new CartItemException("존재하지 않는 장바구니 상품입니다.");
        }

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

        if (Objects.isNull(cartItem)) {
            throw new CartItemException.CartItemNotExists("존재하지 않는 장바구니 상품입니다.");
        }

        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
