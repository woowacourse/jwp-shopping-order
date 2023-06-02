package cart.domain.cartitem.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.cartitem.dto.CartItemQuantityUpdateRequest;
import cart.domain.cartitem.dto.CartItemRequest;
import cart.domain.cartitem.dto.CartItemResponse;
import cart.domain.cartitem.persistence.CartItemDao;
import cart.domain.member.domain.Member;
import cart.domain.member.persistence.MemberDao;
import cart.domain.product.persistence.ProductDao;
import cart.global.config.AuthMember;
import cart.global.exception.CartItemNotFoundException;
import cart.global.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao,
                           MemberDao memberDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
    }

    public List<CartItemResponse> findByMember(AuthMember authMember) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        List<CartItem> cartItems = cartItemDao.findDescByMemberId(findMember.getId());
        return cartItems.stream().map(CartItemResponse::from).collect(Collectors.toList());
    }

    public Long add(AuthMember authMember, CartItemRequest cartItemRequest) {
        Long productId = cartItemRequest.getProductId();
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        checkProductExist(productId);
        Optional<CartItem> nullableCartItem = cartItemDao.selectByMemberIdAndProductId(findMember.getId(), productId);
        if (nullableCartItem.isPresent()) {
            CartItem cartItem = nullableCartItem.get();
            return addQuantityCurrentCartItem(cartItemRequest, cartItem);
        }
        return cartItemDao.save(new CartItem(cartItemRequest.getQuantity(), findMember, productDao.getProductById(cartItemRequest.getProductId())));
    }

    private void checkProductExist(Long id) {
        if (productDao.isNotExistById(id)) {
            throw new ProductNotFoundException("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
        }
    }

    private Long addQuantityCurrentCartItem(CartItemRequest cartItemRequest, CartItem cartItem) {
        cartItem.addQuantity(cartItemRequest.getQuantity());
        cartItemDao.updateQuantity(cartItem);
        return cartItem.getId();
    }

    public void updateQuantity(AuthMember authMember, Long id, CartItemQuantityUpdateRequest request) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        checkCartItemExist(id);
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(findMember);
        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }
        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    private void checkCartItemExist(Long id) {
        if (cartItemDao.isNotExistById(id)) {
            throw new CartItemNotFoundException("장바구니 상품 ID에 해당하는 장바구니 상품을 찾을 수 없습니다.");
        }
    }

    public void remove(AuthMember authMember, Long id) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        checkCartItemExist(id);
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(findMember);

        cartItemDao.deleteById(id);
    }
}
