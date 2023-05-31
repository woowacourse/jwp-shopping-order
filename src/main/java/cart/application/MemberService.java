package cart.application;

import cart.domain.coupon.CouponType;
import cart.domain.member.Member;
import cart.domain.repository.MemberRepository;
import cart.exception.AuthenticationException;
import cart.util.Encryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CouponService couponService;

    public MemberService(MemberRepository memberRepository, CouponService couponService) {
        this.memberRepository = memberRepository;
        this.couponService = couponService;
    }

    @Transactional
    public void join(Member member) {
        Long memberId = memberRepository.save(member);
        couponService.issueCoupon(memberId, CouponType.WELCOME_JOIN);
    }

    public String login(Member member) {
        Member findMember = memberRepository.findByName(member.getName());
        String encryptedPassword = Encryptor.encrypt(member.getPassword());

        if (!findMember.checkPassword(encryptedPassword)) {
            throw new AuthenticationException("Name 및 Password에 일치하는 회원이 없습니다.");
        }

        return findMember.getPassword();
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
