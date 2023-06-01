package cart.domain.member;

import cart.domain.value.Email;
import cart.domain.value.Money;
import cart.domain.value.Password;

public class Member {
    private Long id;
    private Email email;
    private Password password;
    private Rank rank;
    private Money totalPurchaseAmount;

    public Member(final Long id, final Email email, final Password password, final Rank rank, final Money totalPurchaseAmount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rank = rank;
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public Member(final Long id, final String email, final String password, final Rank rank, final int totalPurchaseAmount) {
        this(id, new Email(email), new Password(password), rank, new Money(totalPurchaseAmount));
    }

    public int discountPrice(final int itemPrice) {
        return rank.getDiscountPrice(itemPrice);
    }

    public void update(final int money) {
        int updateMoney = totalPurchaseAmount.plus(money);
        Rank updatedRank = Rank.findRank(updateMoney);
        if (rank != updatedRank) {
            rank = updatedRank;
        }
        totalPurchaseAmount = new Money(updateMoney);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public Rank getRank() {
        return rank;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount.getMoney();
    }

    public boolean checkPassword(final Password password) {
        return this.password.equals(password);
    }

}
