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

    public void addTotalPurchaseAmount(final int value) {
        this.totalPurchaseAmount = new Money(this.totalPurchaseAmount.getValue() + value);
    }

    public void upgradeRank() {
        if (totalPurchaseAmount.getValue() >= 100_000) {
            rank = Rank.SILVER;
        }
        if (totalPurchaseAmount.getValue() >= 200_000) {
            rank = Rank.GOLD;
        }
        if (totalPurchaseAmount.getValue() >= 300_000) {
            rank = Rank.PLATINUM;
        }
        if (totalPurchaseAmount.getValue() >= 500_000) {
            rank = Rank.DIAMOND;
        }
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
