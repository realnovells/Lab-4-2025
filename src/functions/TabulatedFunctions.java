package functions;

import java.io.*;
import java.util.Arrays;

public class TabulatedFunctions {

    //Serializable
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int count = dis.readInt();
        double[] x = new double[count];
        double[] y = new double[count];
        for (int i = 0; i < count; i++) {
            x[i] = dis.readDouble();
            y[i] = dis.readDouble();
        }
        return new ArrayTabulatedFunction(x[0], x[count - 1], y);
    }

    //Externalizable
    public static void writeExternal(TabulatedFunction function, ObjectOutputStream out) throws IOException {
        out.writeInt(function.getPointsCount()); // количество точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.writeDouble(function.getPointX(i));
            out.writeDouble(function.getPointY(i));
        }
    }

    public static TabulatedFunction readExternal(ObjectInputStream in) throws IOException {
        int count = in.readInt();
        double[] x = new double[count];
        double[] y = new double[count];
        for (int i = 0; i < count; i++) {
            x[i] = in.readDouble();
            y[i] = in.readDouble();
        }
        return new ArrayTabulatedFunction(x[0], x[count - 1], y);
    }

    // Tabulate
    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        if (pointsCount < 2) throw new IllegalArgumentException("Количество точек должно быть ≥ 2");
        if (leftX >= rightX) throw new IllegalArgumentException("Левая граница должна быть < правой");

        double step = (rightX - leftX) / (pointsCount - 1);
        double[] y = new double[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            y[i] = f.getFunctionValue(leftX + i * step);
        }
        return new ArrayTabulatedFunction(leftX, rightX, y);
    }
}
