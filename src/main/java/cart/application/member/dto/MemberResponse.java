package cart.application.member.dto;

public class MemberResponse {

	private final Long id;
	private final String email;
	private final String password;

	public MemberResponse(final Long id, final String email, final String password) {
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
