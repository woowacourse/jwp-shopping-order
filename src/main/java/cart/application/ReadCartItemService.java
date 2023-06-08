package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.cartItem.CartItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ReadCartItemService {

    private final CartItemDao cartItemDao;

    public ReadCartItemService(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }
}
