package functions.meta;

import functions.Function;

public class Composition implements Function {

    private Function fOuter; // f
    private Function fInner; // g

    public Composition(Function fOuter, Function fInner) {
        this.fOuter = fOuter;
        this.fInner = fInner;
    }

    @Override
    public double getLeftDomainBorder() {
        return fInner.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return fInner.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return fOuter.getFunctionValue(fInner.getFunctionValue(x));
    }
}
