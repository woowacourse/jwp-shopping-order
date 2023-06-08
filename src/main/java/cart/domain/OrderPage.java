package cart.domain;

public class OrderPage {

    private final int limit;

    public OrderPage(int limit) {
        this.limit = limit;
    }

    public int calculateSkipNumber(int pageNumber) {
        return (pageNumber - 1) * limit;
    }

    public int calculateTotalPages(int totalNumber) {
        return (totalNumber / limit) + 1;
    }

    public int getLimit() {
        return limit;
    }
}
