package cart.domain;

import java.util.List;

public class Orders {
    private long id;
    private final int price;
    private final boolean confirmState;

    public Orders(long id, int price, boolean confirmState) {
        this.id = id;
        this.price = price;
        this.confirmState = confirmState;
    }

    public long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public boolean isConfirmState() {
        return confirmState;
    }
}
