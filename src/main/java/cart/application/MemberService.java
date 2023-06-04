package cart.application;

import cart.dao.MemberDao;
import cart.dao.OrderHistoryDao;
import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.dto.request.MemberCreateRequest;
import cart.dto.response.OrderItemsResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;
    private final OrderHistoryDao orderHistoryDao;

    public MemberService(final MemberDao memberDao, final OrderHistoryDao orderHistoryDao) {
        this.memberDao = memberDao;
        this.orderHistoryDao = orderHistoryDao;
    }

    @Transactional
    public void signUp(MemberCreateRequest request) {
        Member member = new Member(request.getEmail(), request.getPassword());
        memberDao.insert(member);
    }

    public OrderItemsResponse findOrders(Member member) {
        List<OrderHistory> orderHistories = orderHistoryDao.findAllByMemberId(member.getId());
        return OrderItemsResponse.of(orderHistories);
    }
}
