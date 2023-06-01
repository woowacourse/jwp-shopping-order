package cart.repository.member;

import cart.dao.member.MemberDao;
import cart.domain.member.Member;
import cart.entity.member.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findByEmail(final String email) {
        MemberEntity memberEntity = memberDao.findMemberByEmail(email);
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }

    public List<Member> findAll() {
        return memberDao.findAllMembers().stream()
                .map(entity -> new Member(entity.getId(), entity.getEmail(), entity.getPassword()))
                .collect(Collectors.toList());
    }

    public Member findMemberById(final long memberId) {
        MemberEntity memberEntity = memberDao.findMemberById(memberId);
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }

    public boolean isExistMemberById(final Long memberId) {
        return memberDao.isExistMemberById(memberId);
    }

    public boolean isExistMemberByEmail(final String memberEmail) {
        return memberDao.isExistMemberByEmail(memberEmail);
    }

    public long save(final Member member) {
        return memberDao.save(member.getEmail(), member.getPassword());
    }

    public void deleteById(final Long memberId) {
        memberDao.deleteMember(memberId);
    }
}
