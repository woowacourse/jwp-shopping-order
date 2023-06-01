package cart.config.auth.dto;

import cart.domain.member.Member;

public class AuthMember {

	private Long id;
	private String email;
	private String password;

	public AuthMember() {
	}

	public AuthMember(final Member member) {
		this(member.getId(), member.getEmail(), member.getPassword());
	}

	public AuthMember(final Long id, final String email, final String password) {
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
