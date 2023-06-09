package cart.error.exception;

public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(final String message) {
		super(message);
	}

	public static class Email extends UnauthorizedException {
		public Email() {
			super("가입되지 않은 email입니다.");
		}
	}

	public static class Password extends UnauthorizedException {
		public Password() {
			super("잘못된 비밀번호입니다.");
		}
	}

	public static class Null extends UnauthorizedException {
		public Null() {
			super("접근 권한이 없습니다.");
		}
	}
}
