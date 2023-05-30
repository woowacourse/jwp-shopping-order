package cart.exception;

public class OrdersPriceNotMatchException extends RuntimeException{
    public OrdersPriceNotMatchException(String message){
        super(message);
    }
}
