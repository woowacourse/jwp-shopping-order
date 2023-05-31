package cart.repository;

import cart.dao.MemberDao;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.entity.MemberEntity;
import cart.repository.mapper.MemberMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMemberById(final Long id) {
        final MemberEntity memberEntity = memberDao.getMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "id 에 해당하는 멤버를 찾을 수 없습니다."));

        return MemberMapper.toMember(memberEntity);
    }

    public Member getMemberByEmail(final Email email) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(email.email())
                .orElseThrow(() -> new IllegalArgumentException(email.email() + " 이메일을 가진 멤버를 찾을 수 없습니다."));

        return MemberMapper.toMember(memberEntity);
    }

    public List<Member> getAllMembers() {
        final List<MemberEntity> allMembers = memberDao.getAllMembers();
        return allMembers.stream()
                .map(MemberMapper::toMember)
                .collect(Collectors.toUnmodifiableList());
    }
}
