package cart.domain.point;

public class SavePointPolicy implements PointPolicy {

    public int calculate(int payment) {
        PointAccumulationRate pointAccumulationRate = PointAccumulationRate.findBy(payment);
        return (int) Math.floor(payment * pointAccumulationRate.getRate());
    }
}
