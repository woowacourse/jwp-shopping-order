package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import org.springframework.stereotype.Service;

@Service
public class PayService {

    private final MemberDao memberDao;

    public PayService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void pay(Order order, Money deliveryFee, Money discounting, Long id) {
        Order payed = order.confirmOrder(deliveryFee, discounting);
        Member member = memberDao.getMemberById(id);
        member.payMoney(payed);
        memberDao.updateMember(member);
    }
}
