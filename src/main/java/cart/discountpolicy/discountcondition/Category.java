package cart.discountpolicy.discountcondition;

public enum Category {
    CHICKEN, PIZZA, NONE;

    public static Category assignCategory(String name) {
        if (name.contains("피자")) return PIZZA;
        if (name.contains("치킨")) return CHICKEN;
        return NONE;
    }
}
