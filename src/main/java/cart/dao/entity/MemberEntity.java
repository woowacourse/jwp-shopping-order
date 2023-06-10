package cart.dao.entity;

import java.util.Objects;

public class MemberEntity {

    private final long id;
    private final String email;
    private final String password;
    private final int point;
    private final int money;

    public MemberEntity(long id, String email, String password, int point, int money) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPoint() {
        return point;
    }

    public int getMoney() {
        return money;
    }

    public static class Builder {

        private long id;
        private String email;
        private String password;
        private int point;
        private int money;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder point(int point) {
            this.point = point;
            return this;
        }

        public Builder money(int money) {
            this.money = money;
            return this;
        }

        public MemberEntity build() {
            return new MemberEntity(id, email, password, point, money);
        }
    }
}
