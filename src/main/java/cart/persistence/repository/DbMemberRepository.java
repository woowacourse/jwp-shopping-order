package cart.persistence.repository;

import cart.domain.Member;
import cart.domain.MemberRepository;
import cart.exception.NoSuchMemberException;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
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
                .map(this::mapToMember)
                .collect(Collectors.toList());
    }

    @Override
    public Member findById(Long id) {
        MemberEntity memberEntity = memberDao.findById(id).orElseThrow(() -> new NoSuchMemberException()); // TODO null 예외 처리
        return mapToMember(memberEntity);
    }

    @Override
    public Member findByEmail(String email) {
        MemberEntity memberEntity = memberDao.findByEmail(email).orElseThrow(() -> new NoSuchMemberException());
        return mapToMember(memberEntity);
    }

    @Override
    public Long add(Member member) {
        return memberDao.add(mapToMemberEntity(member));
    }

    @Override
    public Long update(Member member) {
        return memberDao.update(mapToMemberEntity(member));
    }

    @Override
    public void delete(Long id) {
        memberDao.delete(id);
    }

    public MemberEntity mapToMemberEntity(Member member) {
        return new MemberEntity(member.getId(), member.getEmail(), member.getPassword());
    }

    public Member mapToMember(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(), memberEntity.getPassword());
    }
}
