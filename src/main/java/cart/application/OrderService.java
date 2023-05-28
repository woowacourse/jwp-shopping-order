package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.domain.member.Member;
import cart.dto.OrderRequest;
import cart.exception.InvalidPointException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private static final int SHIPPING_FEE = 3000;

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao, final OrderDao orderDao) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public Long order(final Member member, final OrderRequest request) {
        final Member findMember = memberDao.getMemberByEmail(member.getEmailValue());
        if (findMember.getPointValue() < request.getPoint()) {
            throw new InvalidPointException(member.getPointValue(), request.getPoint());
        }
//        final List<CartItem> cartItems = cartItemDao.getCartItemsByIds(request.getCartItemIds());

        return null;
    }
}
