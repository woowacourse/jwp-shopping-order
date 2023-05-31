package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.value.Email;
import cart.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member save(final Member member) {
        final MemberEntity memberEntity = memberDao.save(new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getRank().name(),
                member.getTotalPurchaseAmount()
        ));
        return new Member(
                memberEntity.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getTotalPurchaseAmount()
        );
    }

    public List<Member> findAll() {
        return memberDao.findAll()
                .stream()
                .map(memberEntity -> new Member(
                                memberEntity.getId(),
                                memberEntity.getEmail(),
                                memberEntity.getPassword(),
                                memberEntity.getTotalPurchaseAmount()
                        )
                ).collect(Collectors.toList());
    }

    public Member findById(final Long id) {
        final MemberEntity memberEntity = memberDao.findById(id);
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getTotalPurchaseAmount()
        );
    }

    public Member findByEmail(final Email email) {
        final MemberEntity memberEntity = memberDao.findByEmail(email.getValue());
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getTotalPurchaseAmount()
        );
    }

    public void update(final Member member) {
        memberDao.update(member);
    }

    public void deleteById(final Long id) {
        memberDao.deleteById(id);
    }
}
