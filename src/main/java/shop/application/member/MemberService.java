package shop.application.member;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.member.dto.MemberDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.domain.event.MemberJoinedEvent;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;
import shop.exception.AuthenticationException;
import shop.exception.ShoppingException;
import shop.util.Encryptor;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;

    public MemberService(ApplicationEventPublisher eventPublisher, MemberRepository memberRepository) {
        this.eventPublisher = eventPublisher;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void join(MemberJoinDto memberDto) {
        if (memberRepository.isExistMemberByName(memberDto.getName())) {
            throw new ShoppingException("중복되는 이름입니다. 입력한 회원 이름 : " + memberDto.getName());
        }

        Member member = new Member(memberDto.getName(), memberDto.getPassword());

        Long memberId = memberRepository.save(member);
        eventPublisher.publishEvent(new MemberJoinedEvent(memberId));
    }

    public String login(MemberLoginDto memberDto) {
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
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }

}
