package cart.application;

import cart.dao.MemberDao;
import cart.dao.OrderHistoryDao;
import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.dto.request.MemberCreateRequest;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrderItemResponse;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<OrderItemResponse> findOrders(Member member) {
        List<OrderHistory> orderHistories = orderHistoryDao.findAllByMemberId(member.getId());
        return orderHistories.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
    }

    public OrderDetailResponse findOrder(final Member member, final Long orderId) {
        OrderHistory orderHistory = orderHistoryDao.findById(orderId);
        return OrderDetailResponse.of(orderHistory);
    }
}
