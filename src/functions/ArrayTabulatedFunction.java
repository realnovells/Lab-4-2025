package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {

    private FunctionPoint[] points;
    public ArrayTabulatedFunction() {
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть < правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть ≥ 2");
        }

        points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть < правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть ≥ 2");
        }

        points = new FunctionPoint[values.length];
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null) throw new IllegalArgumentException("Массив точек равен null");
        if (points.length < 2) throw new IllegalArgumentException("Количество точек должно быть >= 2");

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки должны быть строго упорядочены по X");
            }
        }

        this.points = new FunctionPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    public ArrayTabulatedFunction(double[] xs, double[] ys) {
        if (xs == null || ys == null)
            throw new IllegalArgumentException("Массивы равны null");
        if (xs.length != ys.length)
            throw new IllegalArgumentException("Массивы должны быть одинаковой длины");
        if (xs.length < 2)
            throw new IllegalArgumentException("Количество точек должно быть >= 2");

        points = new FunctionPoint[xs.length];
        for (int i = 0; i < xs.length; i++) {
            if (i > 0 && xs[i] <= xs[i - 1]) {
                throw new IllegalArgumentException("Массив xs должен быть строго возрастающим");
            }
            points[i] = new FunctionPoint(xs[i], ys[i]);
        }
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ массива точек");
        return new FunctionPoint(points[index]);
    }

    public FunctionPoint[] getPoints() {
        return points;
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[points.length - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) return Double.NaN;

        if (Math.abs(x - points[0].getX()) < 1e-10) return points[0].getY();
        if (Math.abs(x - points[points.length - 1].getX()) < 1e-10) return points[points.length - 1].getY();

        for (int i = 0; i < points.length - 1; i++) {
            double x1 = points[i].getX();
            double y1 = points[i].getY();
            double x2 = points[i + 1].getX();
            double y2 = points[i + 1].getY();

            if (x >= x1 && x <= x2) {
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return points.length;
    }

    public FunctionPoint getPointCopy(int index) {
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ");

        double x = point.getX();
        if ((index > 0 && x <= points[index - 1].getX()) ||
                (index < points.length - 1 && x >= points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("Точка нарушает порядок X-координат");
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ");
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ");
        if ((index > 0 && x <= points[index - 1].getX()) ||
                (index < points.length - 1 && x >= points[index + 1].getX())) return;

        points[index].setX(x);
    }

    public double getPointY(int index) {
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ массива точек");
        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ массива точек");
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        if (points.length < 3)
            throw new IllegalStateException("Нельзя удалить точку, если их меньше трех");
        if (index < 0 || index >= points.length)
            throw new FunctionPointIndexOutOfBoundsException("Индекс вне границ");

        FunctionPoint[] newPoints = new FunctionPoint[points.length - 1];
        System.arraycopy(points, 0, newPoints, 0, index);
        System.arraycopy(points, index + 1, newPoints, index, points.length - index - 1);
        points = newPoints;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();
        for (FunctionPoint p : points) {
            if (Math.abs(p.getX() - x) < 1e-10)
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
        }

        int insertIndex = 0;
        while (insertIndex < points.length && points[insertIndex].getX() < x) insertIndex++;

        FunctionPoint[] newPoints = new FunctionPoint[points.length + 1];
        System.arraycopy(points, 0, newPoints, 0, insertIndex);
        newPoints[insertIndex] = new FunctionPoint(point);
        System.arraycopy(points, insertIndex, newPoints, insertIndex + 1, points.length - insertIndex);
        points = newPoints;
    }

    //Externalizable
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(points.length);
        for (FunctionPoint p : points) {
            out.writeDouble(p.getX());
            out.writeDouble(p.getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        int size = in.readInt();
        points = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }
}
