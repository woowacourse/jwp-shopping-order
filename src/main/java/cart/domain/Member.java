package cart.domain;

import cart.domain.value.Email;
import cart.domain.value.Money;
import cart.domain.value.Password;

public class Member {
    private Long id;
    private Email email;
    private Password password;
    private Rank rank;
    private Money totalPurchaseAmount;

    public Member() {
    }

    public Member(
            final Long id,
            final String email,
            final String password
    ) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.rank = Rank.NORMAL;
        this.totalPurchaseAmount = new Money(0);
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
        this.totalPurchaseAmount = new Money(totalPurchaseAmount);
    }

    public boolean isCorrectPassword(final String value) {
        final Password password = new Password(value);
        return this.password.equals(password);
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

    public Rank getRank() {
        return rank;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount.getValue();
    }
}
