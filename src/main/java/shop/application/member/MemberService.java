package shop.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.coupon.CouponService;
import shop.application.member.dto.MemberDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.domain.coupon.CouponType;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;
import shop.exception.AuthenticationException;
import shop.util.Encryptor;

import java.util.List;
import java.util.stream.Collectors;

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
    public void join(MemberJoinDto memberDto) {
        Member member = new Member(memberDto.getName(), memberDto.getPassword());

        Long memberId = memberRepository.save(member);
        couponService.issueCoupon(memberId, CouponType.WELCOME_JOIN);
    }

    public String login(MemberLoginDto memberDto) {
        Member findMember = memberRepository.findByName(memberDto.getName());
        String encryptedPassword = Encryptor.encrypt(memberDto.getPassword());

        if (!findMember.checkPassword(encryptedPassword)) {
            throw new AuthenticationException("Name 및 Password에 일치하는 회원이 없습니다.");
        }

        return findMember.getPassword();
    }

    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }

}
