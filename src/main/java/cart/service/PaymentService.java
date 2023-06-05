package cart.service;

import cart.domain.Member;
import cart.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final MemberRepository memberRepository;

    public PaymentService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void payByMember(Member member, long usePoint, int totalPrice) {
        long payMoney = totalPrice - usePoint;
        member.usePoint(usePoint);
        member.useMoney(payMoney);
        memberRepository.update(member);
    }
}
