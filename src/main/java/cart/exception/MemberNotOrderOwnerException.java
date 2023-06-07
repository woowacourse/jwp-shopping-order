package cart.exception;

public class MemberNotOrderOwnerException extends RuntimeException {

    public MemberNotOrderOwnerException(final long memberId) {
        super("본인의 주문이 아닙니다. / 요청하신 Member의 id : " + memberId);
    }
}
