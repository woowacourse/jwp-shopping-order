package cart.dao;

import cart.domain.Member;
import cart.domain.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDao memberDao;

    public MemberRepositoryImpl(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<Member> findMemberById(final Long id) {
        final MemberEntity memberEntity = memberDao.findMemberById(id);

        if (memberEntity == null) {
            return Optional.empty();
        }

        return Optional.of(memberEntity.toMember());
    }

    @Override
    public Optional<Member> findMemberByEmail(final String email) {
        final MemberEntity memberEntity = memberDao.findMemberByEmail(email);

        if (memberEntity == null) {
            return Optional.empty();
        }

        return Optional.of(memberEntity.toMember());
    }

    @Override
    public List<Member> findAllMembers() {
        final List<MemberEntity> allMembers = memberDao.findAllMembers();

        return allMembers.stream()
                .map(MemberEntity::toMember)
                .collect(Collectors.toList());
    }

    @Override
    public Long saveMember(final Member member) {
        final MemberEntity memberEntity = MemberEntity.from(member);

        return memberDao.saveMember(memberEntity);
    }
}
