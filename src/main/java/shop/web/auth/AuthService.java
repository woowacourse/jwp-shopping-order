package shop.web.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;
import shop.exception.AuthenticationException;

@Transactional(readOnly = true)
@Service
public class AuthService {
    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void authenticate(String name, String password) {
        Member member = memberRepository.findByName(name);

        if (member.isMatchingPassword(password)) {
            return;
        }

        throw new AuthenticationException("인증되지 않은 사용자입니다.");

    }
}
