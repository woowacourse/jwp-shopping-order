package cart.application.service;

import cart.application.repository.MemberRepository;
import cart.domain.Member;
import cart.exception.LoginFailException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member login(final String email, final String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(LoginFailException::new);
        if (!member.checkPassword(password)) {
            throw new LoginFailException();
        }
        return member;
    }
}
