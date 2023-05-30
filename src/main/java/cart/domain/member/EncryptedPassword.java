package cart.domain.member;

public class EncryptedPassword implements Password {
    private final String password;

    public EncryptedPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
