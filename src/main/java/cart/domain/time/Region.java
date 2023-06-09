package cart.domain.time;

public enum Region {

    KOREA("Asia/Seoul");

    private final String timeZone;

    Region(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
