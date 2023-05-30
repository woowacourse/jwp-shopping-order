package cart.application;

import cart.application.dto.MemberDto;
import cart.domain.member.Member;
import cart.domain.repository.MemberRepository;
import cart.exception.AuthenticationException;
import cart.util.Encryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void join(MemberDto memberDto) {
        Member member = new Member(memberDto.getName(), memberDto.getPassword());

        memberRepository.save(member);
    }

    public String login(MemberDto memberDto) {
        Member findMember = memberRepository.findByName(memberDto.getName());
        String encryptedPassword = Encryptor.encrypt(memberDto.getPassword());

        if (!findMember.checkPassword(encryptedPassword)) {
            throw new AuthenticationException("Name 및 Password에 일치하는 회원이 없습니다.");
        }

        return findMember.getPassword();
    }

    public List<MemberDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }


}
