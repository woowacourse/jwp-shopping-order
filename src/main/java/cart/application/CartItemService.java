package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.dto.AuthMember;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
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
        Member findMember = memberDao.getMemberByEmail(authMember.getEmail());
        List<CartItem> cartItems = cartItemDao.findByMemberId(findMember.getId());
        return cartItems.stream().map(CartItemResponse::from).collect(Collectors.toList());
    }

    public Long add(AuthMember authMember, CartItemRequest cartItemRequest) {
        Member findMember = memberDao.getMemberByEmail(authMember.getEmail());
        return cartItemDao.save(new CartItem(findMember, productDao.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(AuthMember authMember, Long id, CartItemQuantityUpdateRequest request) {
        Member findMember = memberDao.getMemberByEmail(authMember.getEmail());
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(findMember);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(AuthMember authMember, Long id) {
        Member findMember = memberDao.getMemberByEmail(authMember.getEmail());
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(findMember);

        cartItemDao.deleteById(id);
    }
}
