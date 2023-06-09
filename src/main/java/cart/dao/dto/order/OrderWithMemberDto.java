package cart.dao.dto.order;

import java.sql.Timestamp;

public class OrderWithMemberDto {

    private final Long id;
    private final long memberId;
    private final Timestamp createdAt;
    private final String email;
    private final String password;

    public OrderWithMemberDto(Long id, long memberId, Timestamp createdAt, String email,
        String password) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
