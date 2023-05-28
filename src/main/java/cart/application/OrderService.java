package cart.application;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.domain.member.Member;
import cart.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MemberDao memberDao;
    private final OrderDao orderDao;

    public OrderService(final MemberDao memberDao, final OrderDao orderDao) {
        this.memberDao = memberDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public Long order(final Member member, final OrderRequest request) {
        final Member findMember = memberDao.getMemberByEmail(member.getEmailValue());
        if (findMember.getPointValue() < request.getPoint()) {
            throw new IllegalArgumentException("해당 유저가 가지고 있는 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
        }
        return null;
    }
}
