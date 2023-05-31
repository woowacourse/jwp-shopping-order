package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.domain.Member;
import cart.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final MemberDao memberDao;
    
    public OrderRepository(final OrderDao orderDao, final MemberDao memberDao) {
        this.orderDao = orderDao;
        this.memberDao = memberDao;
    }
    
    public Long order(final Member member, final Order order) {
        final Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        final Long memberId = memberByEmail.getId();
        order.order();
        final OrderEntity orderEntity = new OrderEntity(memberId, order.getOriginalPrice(), order.getUsedPoint(), order.getPointToAdd());
        return orderDao.insert(orderEntity);
    }
}
