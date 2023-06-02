package cart.dto;

public class PaymentDto {
    private Long originalPayment;
    private Long finalPayment;
    private Long point;

    public PaymentDto() {
    }

    public PaymentDto(final Long originalPayment, final Long finalPayment, final Long point) {
        this.originalPayment = originalPayment;
        this.finalPayment = finalPayment;
        this.point = point;
    }

    public Long getOriginalPayment() {
        return originalPayment;
    }

    public Long getFinalPayment() {
        return finalPayment;
    }

    public Long getPoint() {
        return point;
    }
}
