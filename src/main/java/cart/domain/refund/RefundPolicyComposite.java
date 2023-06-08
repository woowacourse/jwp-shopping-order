package cart.domain.refund;

import cart.domain.order.Order;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;

public class RefundPolicyComposite {

    private final List<RefundPolicy> refundPolicies;

    public RefundPolicyComposite(final List<RefundPolicy> refundPolicies) {
        this.refundPolicies = refundPolicies;
    }

    public RefundPolicy getRefundPolicies(final Order order, final LocalDateTime currentTime) {
        return refundPolicies.stream()
            .filter(refundPolicy -> refundPolicy.isAvailable(order, currentTime))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ErrorCode.ORDER_CANNOT_CANCEL));
    }
}
