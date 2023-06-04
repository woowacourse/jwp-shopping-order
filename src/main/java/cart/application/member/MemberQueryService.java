package cart.application.member;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.member.dto.MemberResponse;
import cart.config.auth.dto.AuthInfo;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.error.exception.UnauthorizedException;

@Transactional(readOnly = true)
@Service
public class MemberQueryService {

	private final MemberRepository memberRepository;

	public MemberQueryService(final MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public List<MemberResponse> findAll() {
		return memberRepository.findAll().stream()
			.map(this::mapUserToUserDto)
			.collect(Collectors.toList());
	}

	public Member checkLoginMember(final AuthInfo authInfo) {
		Member member = memberRepository.findByEmail(authInfo.getEmail())
			.orElseThrow(UnauthorizedException.Email::new);
		validatePassword(member, authInfo.getPassword());

		return member;
	}

	private void validatePassword(final Member member, final String password) {
		if (!member.checkPassword(password)) {
			throw new UnauthorizedException.Password();
		}
	}

	private MemberResponse mapUserToUserDto(final Member member) {
		return new MemberResponse(member.getId(), member.getEmail(), member.getPassword());
	}

}
