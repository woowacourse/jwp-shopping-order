package cart.domain;

public class Money {
    private int value;

    public Money(int value){
        this.value=value;
    }

    private void validate(int value){
        if(value<0){
            throw new IllegalArgumentException("");
        }
    }
}
