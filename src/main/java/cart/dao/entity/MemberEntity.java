package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberEntity {

    private Long id;
    private String email;
    private String password;
    private Integer point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberEntity(String email, String password, Integer point) {
        this(null, email, password, point);
    }

    public MemberEntity(Long id, String email, String password, Integer point) {
        this(id, email, password, point, null, null);
    }

    public MemberEntity(Long id, String email, String password, Integer point, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
