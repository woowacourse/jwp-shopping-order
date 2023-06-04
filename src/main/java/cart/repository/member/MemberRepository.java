package cart.repository.member;

import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.value.Email;
import cart.domain.value.Money;
import cart.domain.value.Password;
import cart.entity.MemberEntity;
import cart.exception.member.MemberNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findById(final Long id) {
        MemberEntity memberEntity = memberDao.getMemberById(id).orElseThrow(MemberNotFoundException::new);
        return makeMember(memberEntity);
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.getMemberByEmail(email).orElseThrow(MemberNotFoundException::new);
        return makeMember(memberEntity);
    }

    public Long create(final Member member) {
        return memberDao.addMember(makeMemberEntity(member));
    }

    public void update(final Member member) {
        memberDao.updateMember(makeMemberEntity(member));
    }

    public void deleteMember(final Long id) {
        memberDao.deleteMember(id);
    }

    private Member makeMember(final MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                new Email(memberEntity.getEmail()),
                new Password(memberEntity.getPassword()),
                Rank.valueOf(memberEntity.getGrade()),
                new Money(memberEntity.getTotalPurchaseAmount()));
    }

    private MemberEntity makeMemberEntity(final Member member) {
        return new MemberEntity(
                member.getEmail(),
                member.getPassword(),
                member.getRank().name(),
                member.getTotalPurchaseAmount()
        );
    }
}
