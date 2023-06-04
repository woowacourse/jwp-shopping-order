package cart.service.member;

import cart.domain.member.Member;
import cart.dto.member.MemberCreateRequest;
import cart.dto.member.MemberResponse;
import cart.exception.MemberAlreadyExistException;
import cart.repository.cart.CartRepository;
import cart.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    public MemberService(final MemberRepository memberRepository, final CartRepository cartRepository) {
        this.memberRepository = memberRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public void createMember(final MemberCreateRequest request) {
        validateAlreadyExistMember(request);
        long memberId = memberRepository.save(new Member(request.getEmail(), request.getPassword()));
        cartRepository.createMemberCart(memberId);
    }

    private void validateAlreadyExistMember(final MemberCreateRequest request) {
        if (memberRepository.isExistMemberByEmail(request.getEmail())) {
            throw new MemberAlreadyExistException(request.getEmail());
        }
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(final Long memberId) {
        return MemberResponse.from(memberRepository.findMemberById(memberId));
    }

    @Transactional
    public void deleteById(final Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
