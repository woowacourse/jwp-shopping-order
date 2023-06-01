package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import cart.exception.notfound.MemberNotFoundException;
import cart.repository.mapper.MemberMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> getAllMembers() {
        List<MemberEntity> memberEntities = memberDao.getAllMembers();
        return memberEntities.stream()
                .map(MemberMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Member getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email)
                .map(MemberMapper::toDomain)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다. 요청 이메일: " + email));
    }

    public Member getMemberById(Long id) {
        return memberDao.getMemberById(id)
                .map(MemberMapper::toDomain)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    public Member getMemberByEmailAndPassword(String email, String password) {
        return memberDao.getMemberByEmailAndPassword(email, password)
                .map(MemberMapper::toDomain)
                .orElseThrow(
                        () -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다. 요청 이메일: " + email + ", 요청 비밀번호: " + password));
    }

    public void update(Member member) {
        memberDao.update(MemberMapper.toEntity(member));
    }
}
