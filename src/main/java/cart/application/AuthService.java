package cart.application;

import cart.domain.member.MemberRepository;
import cart.domain.member.Member;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public void validateMember(String email, String password) {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new CartException(ErrorCode.AUTHENTICATION));

        if (isWrongPassword(member, password)) {
            throw new CartException(ErrorCode.AUTHENTICATION);
        }
    }

    private boolean isWrongPassword(Member member, String password) {
        return !member.checkPassword(password);
    }
}
