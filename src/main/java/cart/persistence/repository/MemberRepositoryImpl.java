package cart.persistence.repository;

import static cart.persistence.mapper.MemberMapper.convertMember;

import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.member.dto.MemberWithId;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.MemberMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDao memberDao;

    public MemberRepositoryImpl(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public long insert(final Member member) {
        final MemberEntity memberEntity = new MemberEntity(member.getName(), member.getPassword());
        return memberDao.insert(memberEntity);
    }

    @Override
    public Member findById(final Long id) {
        final MemberEntity memberEntity = memberDao.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return convertMember(memberEntity);
    }

    @Override
    public Member findByName(final String name) {
        final MemberEntity memberEntity = memberDao.findByName(name)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return convertMember(memberEntity);
    }

    @Override
    public List<MemberWithId> findAll() {
        return memberDao.findAll().stream()
            .map(MemberMapper::convertMemberRes)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existByName(final String memberName) {
        return memberDao.findByName(memberName).isPresent();
    }
}
