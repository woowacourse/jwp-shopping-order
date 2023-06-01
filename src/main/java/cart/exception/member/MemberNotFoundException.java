package cart.exception.member;

import cart.exception.common.CartException;

public class MemberNotFoundException extends CartException {
    
    public MemberNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}

