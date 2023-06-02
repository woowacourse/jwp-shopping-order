package cart.db.repository;

import cart.db.dao.MemberDao;
import cart.db.entity.MemberEntity;
import cart.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cart.db.mapper.MemberMapper.toDomain;
import static cart.db.mapper.MemberMapper.toEntity;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long save(final Member member) {
        MemberEntity memberEntity = toEntity(member);
        return memberDao.create(memberEntity);
    }

    public List<Member> findAll() {
        List<MemberEntity> memberEntities = memberDao.findAll();
        return toDomain(memberEntities);
    }

    public Member findById(final Long id) {
        MemberEntity memberEntity = memberDao.findById(id);
        return toDomain(memberEntity);
    }

    public Member findByName(final String name) {
        MemberEntity memberEntity = memberDao.findByName(name);
        return toDomain(memberEntity);
    }

    public Boolean existsByMember(final Member member) {
        MemberEntity memberEntity = toEntity(member);
        return memberDao.existsByMember(memberEntity);
    }

    public Boolean existsByName(final String name) {
        return memberDao.existsByName(name);
    }
}
