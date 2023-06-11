package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAll() {
        List<MemberEntity> memberEntities = memberDao.findAll();
        return memberEntities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    public Member findById(Long memberId) {
        MemberEntity memberEntity = memberDao.findById(memberId);
        return convertToDomain(memberEntity);
    }

    public Member findByEmail(String memberEmail) {
        MemberEntity memberEntity = memberDao.findByEmail(memberEmail);
        return convertToDomain(memberEntity);
    }

    public void updatePoint(Member member) {
        memberDao.updatePoint(new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getPoint().getPoint()
        ));
    }

    private Member convertToDomain(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint()
        );
    }
}
