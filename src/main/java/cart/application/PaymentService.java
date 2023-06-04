package cart.application;

import cart.application.Event.RequestPaymentEvent;
import cart.domain.point.Point;
import cart.domain.product.Price;
import cart.exception.PointException;
import cart.repository.PointRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PointRepository pointRepository;

    public PaymentService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @EventListener
    public void pay(final RequestPaymentEvent requestPaymentEvent) {
        validateOverPointThanHave(requestPaymentEvent);
        validateOverPointThanPrice(requestPaymentEvent);
    }

    private void validateOverPointThanHave(final RequestPaymentEvent requestPaymentEvent) {
        final Point usePoint = new Point(requestPaymentEvent.getOrderRequest().getUsePoint());
        final Point memberPoint = pointRepository.findPointByMember(requestPaymentEvent.getMember());

        if (usePoint.isMoreThan(memberPoint)) {
            throw new PointException.BadRequest(memberPoint);
        }
    }

    private void validateOverPointThanPrice(final RequestPaymentEvent requestPaymentEvent) {
        final Price cartPrice = requestPaymentEvent.getPrice();
        final Point usePoint = new Point(requestPaymentEvent.getOrderRequest().getUsePoint());

        if (usePoint.isMoreThan(new Point(cartPrice.price()))) {
            throw new PointException.BadRequest(cartPrice);
        }
    }
}
