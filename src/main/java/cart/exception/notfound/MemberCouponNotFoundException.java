package cart.exception.notfound;

public class MemberCouponNotFoundException extends NotFoundException {

    public MemberCouponNotFoundException() {
        super("쿠폰 정보를 찾을 수 없습니다.");
    }
}
