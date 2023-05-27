package cart.dao.entity;

import java.time.LocalDateTime;

public class MemberEntity {

    private Long id;
    private String email;
    private String password;
    private Integer point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAT;

    public MemberEntity(Long id, String email, String password, Integer point, LocalDateTime createdAt, LocalDateTime updatedAT) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAT = updatedAT;
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

    public LocalDateTime getUpdatedAT() {
        return updatedAT;
    }
}
