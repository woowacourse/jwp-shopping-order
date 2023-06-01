package cart.exception;

public class WrongDiscountTypeInputException extends RuntimeException{

    public WrongDiscountTypeInputException(String input) {
        super("잘못된 할인정책 명이 입력되었습니다. - " + input);
    }
}
