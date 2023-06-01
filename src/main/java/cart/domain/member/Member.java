package cart.domain.member;

import cart.util.Sha256Encryptor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class Member {

    private final Long id;
    private final String name;
    private final Password password;

    public Member(final String name, final String password) {
        this.id = null;
        this.name = name;
        this.password = encrypt(PlainPassword.of(password));
    }

    public Member(final Long id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = EncryptedPassword.of(password);
    }

    private Password encrypt(final Password password) {
        return EncryptedPassword.of(Sha256Encryptor.encrypt(password.getPassword()));
    }

    public String generateToken() {
        String credentials = name + ":" + password.getPassword();
        byte[] encodedBytes = Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password.getPassword();
    }

    public boolean checkPassword(final String password) {
        return this.password.getPassword().equals(password);
    }
}
