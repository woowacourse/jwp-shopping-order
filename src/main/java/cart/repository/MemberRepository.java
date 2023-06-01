package cart.repository;

import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;
    private final MemberCouponDao memberCouponDao;

    public MemberRepository(final MemberDao memberDao, final MemberCouponDao memberCouponDao) {
        this.memberDao = memberDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Member getMemberById(Long id) {
        Member member = memberDao.getMemberById(id);
        List<MemberCoupon> coupons = memberCouponDao.findByMemberId(id);
        return member.setCoupons(coupons);
    }

    public Member getMemberByEmail(String email) {
        Member member = memberDao.getMemberByEmail(email);
        List<MemberCoupon> coupons = memberCouponDao.findByMemberId(member.getId());
        return member.setCoupons(coupons);
    }

    public List<Member> getAllMember() {
        List<Member> members = memberDao.getAllMembers();
        return members.stream()
                .map(member -> member.setCoupons(memberCouponDao.findByMemberId(member.getId())))
                .collect(Collectors.toList());
    }

    public void addMember(Member member) {
        memberDao.addMember(member);
    }

    public void updateMember(Member member) {
        memberDao.updateMember(member);
    }

    public void deleteMember(Long id) {
        memberDao.deleteMember(id);
    }
}
