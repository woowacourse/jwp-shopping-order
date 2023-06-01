package cart.domain.member;

public class EncryptedPassword implements Password {

    private final String password;

    private EncryptedPassword(final String password) {
        this.password = password;
    }

    public static Password of(final String password) {
        return new EncryptedPassword(password);
    }
    
    @Override
    public String getPassword() {
        return password;
    }
}
