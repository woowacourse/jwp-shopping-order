//package cart.repository;
//
//import cart.domain.Order;
//import cart.domain.Payment;
//import cart.domain.PaymentRecord;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class PaymentFakeRepository implements PaymentRepository {
//    private final List<PaymentRecord> payments;
//
//    PaymentFakeRepository() {
//        this.payments = new ArrayList<>();
////        payments.add(new PaymentEntity(1L, 10_000, 0, 3000, 1L));
////        payments.add(new PaymentEntity(2L, 50_000, 5_000, 3500, 3L));
////        payments.add(new PaymentEntity(3L, 45_000, 0, 4000, 2L));
////        payments.add(new PaymentEntity(4L, 60_000, 6_000, 4500, 4L));
//    }
//
//    @Override
//    public Long save(Payment payment, Long orderId) {
////        Long newId = payments.get(payments.size() - 1).getId() + 1;
////        payments.add(PaymentEntity.from(newId, payment, orderId));
//        return newId;
//    }
//
//    @Override
//    public Optional<Payment> findByOrder(Order order) {
//        return payments.stream().filter((entity) -> entity.getOrderId() == order.getId())
//                .findFirst()
//                .map(entity -> entity.toDomain());
//    }
//}
