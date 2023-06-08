package cart.order.persistence;

import cart.common.notFoundException.PaymentNotFoundException;
import cart.member.domain.Member;
import cart.member.persistence.MemberDao;
import cart.order.domain.Payment;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository {
    private final MemberDao memberDao;
    private final PaymentDao paymentDao;

    public PaymentRepository(MemberDao memberDao, PaymentDao paymentDao) {
        this.memberDao = memberDao;
        this.paymentDao = paymentDao;
    }

    public void payment(Long orderId, Payment payment, Member member) {
        paymentDao.save(orderId, payment);
        memberDao.updatePoint(member);
    }

    public Payment findByOrderId(Long orderId) {
        return paymentDao.findByOrderId(orderId)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
