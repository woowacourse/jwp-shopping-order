package cart.repository;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.exception.notfound.MemberNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }


    public Member findByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
    }

    public Long save(final Member member) {
        if (member.getId() == null || memberDao.findById(member.getId()).isEmpty()) {
            return memberDao.insert(member);
        }
        memberDao.update(member);
        return member.getId();
    }
}
