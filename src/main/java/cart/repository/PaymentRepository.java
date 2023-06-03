package cart.repository;

import cart.dao.MemberDao;
import cart.dao.PaymentDao;
import cart.domain.Member;
import cart.domain.Payment;
import cart.exception.notFound.PaymentNotFoundException;
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
