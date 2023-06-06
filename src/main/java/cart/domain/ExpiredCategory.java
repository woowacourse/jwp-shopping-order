package cart.domain;

public enum ExpiredCategory {
    ORDER(7);

    private final int value;

    ExpiredCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
