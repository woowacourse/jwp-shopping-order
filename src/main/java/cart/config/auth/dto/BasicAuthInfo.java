package cart.config.auth.dto;

import org.apache.tomcat.util.codec.binary.Base64;

import cart.error.exception.AuthenticationException;

public class BasicAuthInfo implements AuthInfo {

	private static final String BASIC_TYPE = "Basic";
	private static final String DELIMITER = ":";
	private static final int CREDENTIALS_EMAIL_INDEX = 0;
	private static final int CREDENTIALS_PASSWORD_INDEX = 1;

	private final String email;
	private final String password;

	private BasicAuthInfo(final String email, final String password) {
		this.email = email;
		this.password = password;
	}

	public static BasicAuthInfo from(final String authorization) {
		validateAuthorization(authorization);

		final String authHeaderValue = authorization.substring(BASIC_TYPE.length()).trim();
		final byte[] decodedBytes = Base64.decodeBase64(authHeaderValue);
		final String decodedString = new String(decodedBytes);
		final String[] credentials = decodedString.split(DELIMITER);

		return new BasicAuthInfo(
			credentials[CREDENTIALS_EMAIL_INDEX],
			credentials[CREDENTIALS_PASSWORD_INDEX]);
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	private static void validateAuthorization(final String authorization) {
		if (!authorization.toLowerCase().startsWith(BASIC_TYPE.toLowerCase())) {
			throw new AuthenticationException.Unauthorized();
		}
	}
}
