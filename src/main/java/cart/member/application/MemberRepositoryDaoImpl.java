package cart.member.application;

import cart.member.Member;
import cart.member.infrastructure.MemberDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositoryDaoImpl implements MemberRepository {
    private final MemberDao memberDao;

    public MemberRepositoryDaoImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Member getMemberById(Long id) {
        final var memberById = memberDao.getMemberById(id);
        return new Member(memberById.getId(),
                memberById.getEmail(),
                memberById.getPassword());
    }

    @Override
    public Member getMemberByEmail(String email) {
        final var memberByEmail = memberDao.getMemberByEmail(email);
        return new Member(memberByEmail.getId(),
                memberByEmail.getEmail(),
                memberByEmail.getPassword());
    }

    @Override
    public Long addMember(Member member) {
        return memberDao.addMember(member);
    }

    @Override
    public void updateMember(Member member) {
        memberDao.updateMember(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberDao.deleteMember(id);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDao.getAllMembers();
    }
}
