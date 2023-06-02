package cart.domain.member;

public class Member {
    private static final int INITITAL_CASH = 5000;
    private final Long id;
    private final String email;
    private final String password;
    private final Cash cash;

    public Member(final Long id, final String email,
                  final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.cash = new Cash(INITITAL_CASH);
    }

    public Member(final Long id, final String email,
                  final String password, final int cash) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.cash = new Cash(cash);
    }

    public Member chargeCash(int cashToCharge) {
        return new Member(this.id, this.email, this.password, cash.charge(cashToCharge).getCash());
    }

    public void checkPayable(int cashToPay) {
        if (this.cash.isLessThan(cashToPay)) {
            throw new IllegalArgumentException("사용자의 현재 금액이 주문 금액보다 적습니다.");
        }
    }

    public Member pay(int cashToOrder) {
        Cash cashAfterPay = cash.pay(cashToOrder);
        return new Member(id, email, password, cashAfterPay.getCash());
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

    public int getCash() {
        return cash.getCash();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", cash=" + cash +
                '}';
    }
}
