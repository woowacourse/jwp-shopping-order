package cart.application;

import cart.application.event.PaymentRequestEvent;
import cart.domain.point.Point;
import cart.domain.product.Price;
import cart.exception.PointException;
import cart.repository.PointRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PointRepository pointRepository;

    public PaymentService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @EventListener
    @Transactional(readOnly = true)
    public void pay(final PaymentRequestEvent paymentRequestEvent) {
        validateOverPointThanHave(paymentRequestEvent);
        validateOverPointThanPrice(paymentRequestEvent);
    }

    private void validateOverPointThanHave(final PaymentRequestEvent paymentRequestEvent) {
        final Point usePoint = new Point(paymentRequestEvent.getOrderRequest().getUsePoint());
        final Point memberPoint = pointRepository.findPointByMember(paymentRequestEvent.getMember());

        if (usePoint.isMoreThan(memberPoint)) {
            throw new PointException.BadRequest(memberPoint);
        }
    }

    private void validateOverPointThanPrice(final PaymentRequestEvent paymentRequestEvent) {
        final Price cartPrice = paymentRequestEvent.getPrice();
        final Point usePoint = new Point(paymentRequestEvent.getOrderRequest().getUsePoint());

        if (usePoint.isMoreThan(new Point(cartPrice.price()))) {
            throw new PointException.BadRequest(cartPrice);
        }
    }
}
