package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final int point;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MemberEntity(final Long id, final String email, final String password, final int point,
                        final LocalDateTime createdAt,
                        final LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MemberEntity(final Long id, final String email, final String password, final int point) {
        this(id, email, password, point, null, null);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
