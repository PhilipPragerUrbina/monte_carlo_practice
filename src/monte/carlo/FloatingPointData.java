package monte.carlo;
//floating point data
public class FloatingPointData implements Data{
    private double value;
    public FloatingPointData(double value){
        this.value = value;
    }

    public double getValue(){
        return value;
    }

    @Override
    public String toStringValue() {
        return "" +value;
    }

    @Override
    public double toNumericalValue() {
        return value;
    }
}
