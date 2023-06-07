package cart.exception;

public class MemberNotCartOwnerException extends RuntimeException {

    public MemberNotCartOwnerException(final long cartOwnerId, final long requestId) {
        super("요청에 대하여 일치하지 않는 유저입니다. 로그인 정보를 확인해주세요. / 카트를 소유한 Member의 id : " + cartOwnerId + " 요청하신 Member의 id : " + requestId);
    }
}
