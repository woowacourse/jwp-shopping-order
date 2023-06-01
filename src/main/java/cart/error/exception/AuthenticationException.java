package cart.error.exception;

public class AuthenticationException extends RuntimeException {

	public AuthenticationException(final String message) {
		super(message);
	}

	public static class Unauthorized extends AuthenticationException {

		public Unauthorized() {
			super("접근 권한이 없습니다.");
		}
	}

	public static class NotFound extends AuthenticationException {
		public NotFound() {
			super("가입되지 않은 email입니다.");
		}
	}

	public static class BadRequest extends AuthenticationException {
		public BadRequest() {
			super("잘못된 비밀번호입니다.");
		}
	}


}
