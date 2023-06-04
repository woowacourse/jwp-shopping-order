package cart.application;

import cart.dto.User;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public User getAuthenticate(String email, String password) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("해당되는 계정이 없거나 비밀번호가 틀립니다."));
        if (!member.checkPassword(password)) {
            throw new AuthenticationException("해당되는 계정이 없거나 비밀번호가 틀립니다.");
        }
        return new User(member.getId(), member.getEmail());
    }

    public long getMemberPoint(Long memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
        return member.getPoint().getAmount();
    }
}
