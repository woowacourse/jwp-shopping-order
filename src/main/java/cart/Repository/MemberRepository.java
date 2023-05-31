package cart.Repository;

import cart.dao.MemberDao;
import cart.domain.Member.Email;
import cart.domain.Member.Member;
import cart.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;
    private final MemberMapper memberMapper;

    public MemberRepository(final MemberDao memberDao, MemberMapper memberMapper) {
        this.memberDao = memberDao;
        this.memberMapper = memberMapper;
    }

    public Member getMemberById(final Long id) {
        final MemberEntity memberEntity = memberDao.getMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "id 에 해당하는 멤버를 찾을 수 없습니다."));

        return memberMapper.toMember(memberEntity);
    }

    public Member getMemberByEmail(final Email email) {
        final MemberEntity memberEntity = memberDao.getMemberByEmail(email.email())
                .orElseThrow(() -> new IllegalArgumentException(email.email() + " 이메일을 가진 멤버를 찾을 수 없습니다."));

        return memberMapper.toMember(memberEntity);
    }

    public List<Member> getAllMembers() {
        final List<MemberEntity> allMembers = memberDao.getAllMembers();
        return allMembers.stream().map(memberMapper::toMember)
                .collect(Collectors.toUnmodifiableList());
    }
}
