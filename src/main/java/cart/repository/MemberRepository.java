package cart.repository;

import cart.dao.MemberDao;
import cart.dao.dto.member.MemberDto;
import cart.domain.Member;
import cart.exception.authexception.AuthenticationException;
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

    public Member findById(long id) {
        MemberDto memberDto = memberDao.getMemberById(id)
            .orElseThrow(AuthenticationException::new);

        return MemberMapper.toMember(memberDto);
    }

    public Member findByEmail(String email) {
        MemberDto memberDto = memberDao.getMemberByEmail(email)
            .orElseThrow(AuthenticationException::new);

        return MemberMapper.toMember(memberDto);
    }

    public List<Member> findAll() {
        List<MemberDto> allMembers = memberDao.getAllMembers();
        return allMembers.stream()
            .map(MemberMapper::toMember)
            .collect(Collectors.toUnmodifiableList());
    }

    public boolean isEmailAndPasswordExist(String email, String password) {
        return memberDao.isEmailAndPasswordExist(email, password);
    }
}
