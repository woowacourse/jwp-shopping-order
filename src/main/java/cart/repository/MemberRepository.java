package cart.repository;

import cart.dao.MemberDao;
import cart.dao.dto.MemberDto;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findById(final Long id) {
        final MemberDto memberDto = memberDao.getMemberById(id);
        return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getPassword());
    }

    public Member findByEmail(final String email) {
        final MemberDto memberDto = memberDao.getMemberByEmail(email);
        return new Member(memberDto.getId(), memberDto.getEmail(), memberDto.getPassword());
    }
}
