package functions.basic;

import functions.Function;

public class Exp implements Function {

    @Override
    public double getLeftDomainBorder() {
        return -Double.MAX_VALUE; // граница области определения экспоненты
    }

    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.exp(x);
    }
}
