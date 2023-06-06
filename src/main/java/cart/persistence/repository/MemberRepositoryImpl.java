package cart.persistence.repository;

import static cart.persistence.mapper.MemberMapper.convertMember;

import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.MemberMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDao memberDao;
    private final MemberCouponDao memberCouponDao;

    public MemberRepositoryImpl(final MemberDao memberDao, final MemberCouponDao memberCouponDao) {
        this.memberDao = memberDao;
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public long insert(final Member member) {
        final MemberEntity memberEntity = new MemberEntity(member.name(), member.password());
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
        final MemberEntity memberEntity = getMemberEntityByName(name);
        return convertMember(memberEntity);
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll().stream()
            .map(MemberMapper::convertMember)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existByName(final String memberName) {
        return memberDao.findByName(memberName).isPresent();
    }

    @Override
    public Member findMyCouponsByName(final String memberName) {
        final MemberEntity memberEntity = getMemberEntityByName(memberName);
        final List<MemberCouponDto> myCouponsByName = memberCouponDao.findMyCouponsByName(memberName);
        return convertMember(myCouponsByName, memberEntity);
    }

    private MemberEntity getMemberEntityByName(final String name) {
        return memberDao.findByName(name)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
