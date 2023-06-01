package cart.repository;

import cart.dao.MemberDao;
import cart.dao.dto.MemberDto;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;
    private final CouponRepository couponRepository;

    public MemberRepository(final MemberDao memberDao, final CouponRepository couponRepository) {
        this.memberDao = memberDao;
        this.couponRepository = couponRepository;
    }

    public Member findById(final Long id) {
        final MemberDto memberDto = memberDao.getMemberById(id);
        final List<Coupon> coupons = couponRepository.findByMemberId(id);
        return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getPassword(), coupons);
    }

    public Member findByEmail(final String email) {
        final MemberDto memberDto = memberDao.getMemberByEmail(email);
        final List<Coupon> coupons = couponRepository.findByMemberId(memberDto.getId());
        return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getPassword(), coupons);
    }
}
