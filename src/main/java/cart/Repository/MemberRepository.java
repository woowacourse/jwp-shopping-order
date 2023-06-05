package cart.Repository;

import cart.Repository.mapper.MemberMapper;
import cart.dao.MemberDao;
import cart.domain.Member.Email;
import cart.domain.Member.Member;
import cart.entity.MemberEntity;
import cart.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static cart.Repository.mapper.MemberMapper.toMember;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMemberById(final Long id) {
        final MemberEntity memberEntity = memberDao.getMemberById(id)
                .orElseThrow(() -> new NotFoundException.Member(id));

        return toMember(memberEntity);
    }

    public Member getMemberByEmail(final Email email) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(email.email())
                .orElseThrow(() -> new NotFoundException.Member(email.email()));

        return toMember(memberEntity);
    }

    public List<Member> getAllMembers() {
        final List<MemberEntity> allMembers = memberDao.getAllMembers();
        return allMembers.stream().map(MemberMapper::toMember)
                .collect(Collectors.toUnmodifiableList());
    }
}
