package monte.carlo;

public class EmptyData implements Data {
    @Override
    public double toNumericalValue() {
        return 0;
    }

    @Override
    public String toStringValue() {
        return "";
    }
}