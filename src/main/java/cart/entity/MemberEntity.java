package cart.entity;

import cart.domain.Member;
import cart.domain.vo.Money;

import java.math.BigDecimal;
import java.util.Objects;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final BigDecimal money;
    private final BigDecimal point;

    public MemberEntity(String email, String password, BigDecimal money, BigDecimal point) {
        this(null, email, password, money, point);
    }

    public MemberEntity(Long id, String email, String password, BigDecimal money, BigDecimal point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.point = point;
    }

    public Member toDomain() {
        return new Member(id, email, password, Money.from(money), Money.from(point));
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

    public BigDecimal getMoney() {
        return money;
    }

    public BigDecimal getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", money=" + money +
                ", point=" + point +
                '}';
    }
}
