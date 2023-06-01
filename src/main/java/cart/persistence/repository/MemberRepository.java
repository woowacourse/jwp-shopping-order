package cart.persistence.repository;

import cart.domain.member.Member;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cart.persistence.repository.Mapper.memberMapper;

@Component
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> getAllMembers() {
        final List<MemberEntity> members = memberDao.getAllMembers();
        return members.stream()
                .map(Mapper::memberMapper)
                .collect(Collectors.toList());
    }

    public Member getMemberById(final Long memberId) {
        final MemberEntity memberEntity = memberDao.getMemberById(memberId);
        return memberMapper(memberEntity);
    }
}
