package cart.domain;

public enum Grade {

    GENERAL("일반",0, 0),
    SILVER("silver",5, 100_000),
    GOLD("gold",10, 200_000),
    PLATINUM("platinum",15, 300_000),
    DIAMOND("diamond", 20, 500_000);

    private String gradeName;
    private int gradeDiscount;
    private int totalPurchaseAmount;

    Grade(String gradeName, int gradeDiscount, int totalPurchaseAmount) {
        this.gradeName = gradeName;
        this.gradeDiscount = gradeDiscount;
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public static int findGradeDiscount(String grade){
        if (grade.equals(GENERAL.gradeName)) {
            return GENERAL.gradeDiscount;
        }else if(grade.equals(SILVER.gradeName)){
            return SILVER.gradeDiscount;
        }else if(grade.equals(GOLD.gradeName)){
            return GOLD.gradeDiscount;
        }else if(grade.equals(PLATINUM.gradeName)){
            return PLATINUM.gradeDiscount;
        }else{
            return DIAMOND.gradeDiscount;
        }
    }

    public static String findGrade(int totalPurchaseAmount){
        if (totalPurchaseAmount == GENERAL.getTotalPurchaseAmount()) {
            return GENERAL.getGradeName();
        }else if(totalPurchaseAmount <= GOLD.getTotalPurchaseAmount()){
            return SILVER.getGradeName();
        }else if(totalPurchaseAmount <= PLATINUM.getTotalPurchaseAmount()){
            return GOLD.getGradeName();
        }else if(totalPurchaseAmount <= DIAMOND.getTotalPurchaseAmount()){
            return PLATINUM.getGradeName();
        }else{
            return DIAMOND.getGradeName();
        }
    }

    public String getGradeName() {
        return gradeName;
    }

    public int getGradeDiscount() {
        return gradeDiscount;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }
}
