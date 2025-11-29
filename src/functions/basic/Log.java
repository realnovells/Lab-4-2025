package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;

    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть >0 и !=1");
        }
        this.base = base;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0; // логарифм определён только для положительных x
    }

    @Override
    public double getRightDomainBorder() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN; // логарифм отрицательного числа не определён
        }
        return Math.log(x) / Math.log(base);
    }
}
