package monte.carlo;
//Integer data
public class IntData implements Data{
    private int value;
    public IntData(int value){
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
