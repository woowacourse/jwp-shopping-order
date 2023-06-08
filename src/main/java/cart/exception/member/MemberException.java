package cart.exception.member;

import cart.exception.BadRequestException;

public class MemberException {

    public static class NotExistMember extends BadRequestException {
        public NotExistMember() {
            super("존재하지 않는 회원입니다");
        }
    }
}
