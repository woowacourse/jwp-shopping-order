package cart.exception;

import cart.domain.Member;
import cart.domain.purchaseorder.PurchaseOrderInfo;

public class PurchaseOrderException extends RuntimeException {
    public PurchaseOrderException(String message) {
        super(message);
    }

    public static class IllegalMember extends PurchaseOrderException {
        public IllegalMember(PurchaseOrderInfo purchaseOrder, Member member) {
            super("Illegal member attempts to cart; purchaseOrderId=" + purchaseOrder.getId() + ", memberId=" + member.getId());
        }
    }
}
