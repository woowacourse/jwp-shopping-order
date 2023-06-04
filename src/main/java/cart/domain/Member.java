package cart.domain;

import cart.domain.value.Email;
import cart.domain.value.Money;
import cart.domain.value.Password;

public class Member {
    private Long id;
    private Email email;
    private Password password;
    private Grade grade;
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
        this.grade = Grade.NORMAL;
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
        this.grade = Grade.NORMAL;
        this.totalPurchaseAmount = new Money(totalPurchaseAmount);
    }

    public Member(
            final Long id,
            final String email,
            final String password,
            final Grade grade,
            final int totalPurchaseAmount
    ) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.grade = grade;
        this.totalPurchaseAmount = new Money(totalPurchaseAmount);
    }

    public boolean isSamePassword(final String value) {
        final Password password = new Password(value);
        return this.password.equals(password);
    }

    public void addTotalPurchaseAmount(final int value) {
        this.totalPurchaseAmount = new Money(this.totalPurchaseAmount.getValue() + value);
    }

    public void upgradeGrade() {
        if (totalPurchaseAmount.getValue() >= 100_000) {
            grade = Grade.SILVER;
        }
        if (totalPurchaseAmount.getValue() >= 200_000) {
            grade = Grade.GOLD;
        }
        if (totalPurchaseAmount.getValue() >= 300_000) {
            grade = Grade.PLATINUM;
        }
        if (totalPurchaseAmount.getValue() >= 500_000) {
            grade = Grade.DIAMOND;
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

    public Grade getGrade() {
        return grade;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount.getValue();
    }
}
