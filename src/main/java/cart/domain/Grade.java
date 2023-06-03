package cart.domain;

public enum Grade {

    GENERAL("일반",0),

    SILVER("silver",5),
    GOLD("gold",10),
    PLATINUM("platinum",15),
    DIAMOND("diamond", 20);

    private String gradeName;
    private int gradeDiscount;

    Grade(String gradeName, int gradeDiscount) {
        this.gradeName = gradeName;
        this.gradeDiscount = gradeDiscount;
    }

    public String getGradeName() {
        return gradeName;
    }

    public int getGradeDiscount() {
        return gradeDiscount;
    }
    
    public static int findGradeDiscount(String grade){
        if (grade.equals(Grade.GENERAL.gradeName)) {
            return 0;
        }else if(grade.equals(Grade.SILVER.gradeName)){
            return 5;
        }else if(grade.equals(Grade.GOLD.gradeName)){
            return 10;
        }else if(grade.equals(Grade.PLATINUM.gradeName)){
            return 15;
        }else{
            return 20;
        }
    }

    public static String findGrade(int totalPurchaseAmount){
        if (totalPurchaseAmount == 0) {
            return Grade.GENERAL.getGradeName();
        }else if(totalPurchaseAmount <= 100_000){
            return Grade.SILVER.getGradeName();
        }else if(totalPurchaseAmount <= 200_000){
            return Grade.GOLD.getGradeName();
        }else if(totalPurchaseAmount <= 300_000){
            return Grade.PLATINUM.getGradeName();
        }else{
            return Grade.DIAMOND.getGradeName();
        }
    }
}
