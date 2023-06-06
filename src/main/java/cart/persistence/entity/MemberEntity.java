package cart.persistence.entity;

import java.time.LocalDateTime;

public class MemberEntity {

    private final Long id;
    private final String email;
    private final String password;
    private final Integer point;
    private final LocalDateTime createdAt;

    public MemberEntity(final Long id, final String email, final String password, final Integer point, final LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
        this.createdAt = createdAt;
    }

    public MemberEntity(final Long id, final String email, final String password, final int point) {
        this(id, email, password, point, null);
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

    public Integer getPoint() {
        return point;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
