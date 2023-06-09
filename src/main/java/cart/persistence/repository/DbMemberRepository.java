package cart.persistence.repository;

import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.exception.NoSuchMemberException;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbMemberRepository implements MemberRepository {
    private final MemberDao memberDao;

    public DbMemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public List<Member> findAll() {
        List<MemberEntity> memberEntities = memberDao.findAll();
        return memberEntities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Member findById(Long id) {
        MemberEntity memberEntity = memberDao.findById(id).orElseThrow(() -> new NoSuchMemberException());
        return MemberMapper.toDomain(memberEntity);
    }

    @Override
    public Member findByEmail(String email) {
        MemberEntity memberEntity = memberDao.findByEmail(email).orElseThrow(() -> new NoSuchMemberException());
        return MemberMapper.toDomain(memberEntity);
    }

    @Override
    public Long add(Member member) {
        return memberDao.add(MemberMapper.toEntity(member));
    }

    @Override
    public Long update(Member member) {
        return memberDao.update(MemberMapper.toEntity(member));
    }

    @Override
    public void delete(Long id) {
        memberDao.delete(id);
    }
}
