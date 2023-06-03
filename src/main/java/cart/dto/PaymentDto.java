package cart.dto;

import cart.domain.order.Order;

import javax.validation.constraints.NotNull;

public class PaymentDto {

    @NotNull(message = "초기 결제금액이 입력되지 않았습니다.")
    private Integer originalPayment;
    @NotNull(message = "최종 결제금액이 입력되지 않았습니다.")
    private Integer finalPayment;
    @NotNull(message = "사용 포인트가 입력되지 않았습니다.")
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
