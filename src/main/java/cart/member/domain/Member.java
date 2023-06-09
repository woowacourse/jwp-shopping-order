package cart.member.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Long cash;

    private Member(final Long id, final String email, final String password, final Long cash) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.cash = cash;
    }

    public static Member of(final Long id, final String email, final String password, final Long cash) {
        return new Member(id, email, password, cash);
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public void deposit(final Long money) {
        cash += money;
    }

    public void withdraw(final Long money) {
        if (cash - money < 0) {
            throw new IllegalArgumentException("캐시는 0원 미만이 될 수 없습니다");
        }

        cash -= money;
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

    public Long getCash() {
        return cash;
    }
}
