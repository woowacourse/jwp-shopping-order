//package cart.dao.entity;
//
//import cart.domain.Money;
//import cart.domain.Payment;
//
//public class PaymentEntity {
//    private final Long id;
//    private final int originPrice;
//    private final int discountAmount;
//    private final int deliveryFee;
//    private final Long orderId;
//
//    public PaymentEntity(Long id, int originPrice, int discountAmount, int deliveryFee, Long orderId) {
//        this.id = id;
//        this.originPrice = originPrice;
//        this.discountAmount = discountAmount;
//        this.deliveryFee = deliveryFee;
//        this.orderId = orderId;
//    }
//
//    public static PaymentEntity from(Long id, Payment payment, Long orderId) {
//        return new PaymentEntity(id, payment.getOriginalTotalPrice().getValue(), payment.getDiscountAmount().getValue(),
//                payment.getDeliveryFee().getValue(), orderId);
//    }
//
//    public Payment toDomain() {
//        return new Payment(id, Money.from(originPrice), Money.from(discountAmount), Money.from(deliveryFee));
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public int getOriginPrice() {
//        return originPrice;
//    }
//
//    public int getDiscountAmount() {
//        return discountAmount;
//    }
//
//    public int getDeliveryFee() {
//        return deliveryFee;
//    }
//
//    public Long getOrderId() {
//        return orderId;
//    }
//}
