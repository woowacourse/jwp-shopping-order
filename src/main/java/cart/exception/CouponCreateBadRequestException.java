package cart.exception;

public class CouponCreateBadRequestException extends RuntimeException {

    public CouponCreateBadRequestException() {
        super("쿠폰 생성시 정률/정액 정책과, 입력하신 값이 유효하는지 확인해주세요. 정률일 경우 1 ~ 100%까지 설정 가능합니다.");
    }
}
