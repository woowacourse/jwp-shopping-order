package cart.domain;

public class Payment {
    private int totalProductPrice;
    private int totalDeliveryFee;
    private int usePoint;
    private int totalPrice;

    public Payment(int totalProductPrice, int totalDeliveryFee, int usePoint, int totalPrice) {
        this.totalProductPrice = totalProductPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.usePoint = usePoint;
        this.totalPrice = totalPrice;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public int getUsePoint() {
        return usePoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
