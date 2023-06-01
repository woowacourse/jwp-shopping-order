package cart.infrastructure;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.repository.MemberRepository;
import cart.entity.MemberEntity;
import cart.exception.MemberException;
import cart.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public JdbcMemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<Member> findById(Long id) {
        MemberEntity memberEntity = memberDao.getById(id)
                .orElseThrow(MemberException.NotFound::new);

        return Optional.of(MemberMapper.toMember(memberEntity));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        MemberEntity memberEntity = memberDao.getByEmail(email)
                .orElseThrow(MemberException.NotFound::new);

        return Optional.of(MemberMapper.toMember(memberEntity));
    }

    @Override
    public List<Member> findAll() {
        List<MemberEntity> memberEntities = memberDao.getAll();

        return memberEntities.stream()
                .map(MemberMapper::toMember)
                .collect(Collectors.toList());
    }

    @Override
    public Long create(Member member) {
        return memberDao.insert(new MemberEntity(member.getEmail(), member.getPassword(), member.getMoney(), member.getPoint()));
    }

    @Override
    public void update(Member member) {
        memberDao.update(MemberMapper.toEntity(member));
    }
}
