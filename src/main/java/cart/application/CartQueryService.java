package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.dto.CartItemResponse;
import java.util.List;
import java.util.stream.Collectors;
import member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartQueryService {

    private final CartItemDao cartItemDao;

    public CartQueryService(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

}
