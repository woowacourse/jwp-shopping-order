package cart.dto;

import cart.domain.Order;

public class PaymentDto {

    private Integer originalPayment;
    private Integer finalPayment;
    private Integer point;

    public PaymentDto() {
    }

    public PaymentDto(final Integer originalPayment, final Integer finalPayment, final Integer point) {
        this.originalPayment = originalPayment;
        this.finalPayment = finalPayment;
        this.point = point;
    }

    public static PaymentDto from(final Order order) {
        final int finalPayment = order.getPaymentAmount();
        final int point = order.getPointAmount();
        final int originalPayment = finalPayment + point;
        return new PaymentDto(originalPayment, finalPayment, point);
    }

    public Integer getOriginalPayment() {
        return originalPayment;
    }

    public Integer getFinalPayment() {
        return finalPayment;
    }

    public Integer getPoint() {
        return point;
    }
}
