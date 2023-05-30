package cart.domain;

import cart.domain.value.Email;
import cart.domain.value.Password;

public class Member {
    private Long id;
    private Email email;
    private Password password;
    private Rank rank;

    public Member(final Long id, final Email email, final Password password, final Rank rank) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rank = rank;
    }

    public Member(final Long id, final String email, final String password, final Rank rank) {
        this(id, new Email(email), new Password(password), rank);
    }

    public int discountPrice(final int itemPrice) {
        return rank.getDiscountPrice(itemPrice);
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

    public boolean checkPassword(final Password password) {
        return this.password.equals(password);
    }
}
