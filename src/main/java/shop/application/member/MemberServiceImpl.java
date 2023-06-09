package shop.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import shop.application.coupon.CouponService;
import shop.application.member.dto.MemberDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.domain.coupon.CouponType;
import shop.domain.member.Member;
import shop.domain.member.MemberName;
import shop.domain.member.Password;
import shop.domain.repository.MemberRepository;
import shop.exception.AuthenticationException;
import shop.exception.ShoppingException;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    private final CouponService couponService;
    private final MemberRepository memberRepository;

    public MemberServiceImpl(CouponService couponService, MemberRepository memberRepository) {
        this.couponService = couponService;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public void join(MemberJoinDto memberDto) {
        if (memberRepository.isExistMemberByName(memberDto.getName())) {
            throw new ShoppingException("중복되는 이름입니다. 입력한 회원 이름 : " + memberDto.getName());
        }

        Member member = createMember(memberDto);

        Long memberId = memberRepository.save(member);
        couponService.issueCoupon(memberId, CouponType.WELCOME_JOIN);
    }

    private Member createMember(MemberJoinDto memberDto) {
        MemberName name = new MemberName(memberDto.getName());
        Password password = Password.createFromNaturalPassword(memberDto.getPassword());

        return new Member(name, password);
    }

    @Override
    public String login(MemberLoginDto memberDto) {
        Member findMember = memberRepository.findByName(memberDto.getName());
        Password password = Password.createFromNaturalPassword(memberDto.getPassword());

        if (!findMember.isMatchingPassword(password)) {
            throw new AuthenticationException("Name 및 Password에 일치하는 회원이 없습니다.");
        }

        return createLoginToken(findMember);
    }

    private String createLoginToken(Member findMember) {
        String token = findMember.getName() + ":" + findMember.getPassword();

        return Base64Utils.encodeToUrlSafeString(token.getBytes());
    }

    @Override
    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return MemberDto.of(members);
    }
}
