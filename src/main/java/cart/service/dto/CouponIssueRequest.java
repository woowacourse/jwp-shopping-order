package cart.service.dto;

public class CouponIssueRequest {

    private Long id;

    private CouponIssueRequest() {
    }

    public CouponIssueRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
