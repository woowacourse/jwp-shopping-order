package cart.domain;

public enum MemberGrade {
    GOLD,
    SILVER,
    BRONZE,
    NOTHING;

    public static MemberGrade of(final String gradeString) {
        return MemberGrade.valueOf(gradeString.toUpperCase());
    }
}
