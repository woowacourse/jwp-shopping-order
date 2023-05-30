package cart.domain;

import cart.domain.value.Email;
import cart.domain.value.Password;
import cart.domain.value.TotalPurchaseAmount;

public class Member {
    private final Long id;
    private final Email email;
    private final Password password;
    private final Rank rank;
    private final TotalPurchaseAmount totalPurchaseAmount;

    public Member(
            final Long id,
            final String email,
            final String password
    ) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.rank = Rank.NORMAL;
        this.totalPurchaseAmount = new TotalPurchaseAmount(0);
    }

    public Member(
            final Long id,
            final String email,
            final String password,
            final int totalPurchaseAmount
    ) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.rank = Rank.NORMAL;
        this.totalPurchaseAmount = new TotalPurchaseAmount(totalPurchaseAmount);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public boolean checkPassword(final String value) {
        final Password password = new Password(value);
        return this.password.equals(password);
    }
}
