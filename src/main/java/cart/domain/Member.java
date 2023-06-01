package cart.domain;

import cart.exception.CannotPayException;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Money money;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = Money.from(0);
    }

    public Member(Long id, String email, String password, int money) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = Money.from(money);
    }

    public void payMoney(Order payingOrder) {
        try {
            this.money.minus(payingOrder.getPrice());
        } catch (IllegalArgumentException e) {
            throw new CannotPayException();
        }
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

    public Money getMoney() {
        return money;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}
