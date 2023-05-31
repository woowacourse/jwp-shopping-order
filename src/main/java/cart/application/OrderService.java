package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;

    public OrderService(MemberDao memberDao, CartItemDao cartItemDao) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }

    public Long orderItems(Long memberId, OrderRequest orderRequest) {
        Order order = new Order(memberDao.getMemberById(memberId),
                cartItemDao.findByMemberId(memberId),
                orderRequest.getOriginalPrice(),
                orderRequest.getUsedPoint(),
                orderRequest.getPointToAdd());

        return null;
    }

    public List<OrderResponse> findOrdersByMember(Member member) {
        return null;
    }

    public OrderResponse findOrderDetail(Long orderId) {
        return null;
    }
}
