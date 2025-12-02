package functions;

import java.io.*;
import java.util.Arrays;

public class TabulatedFunctions {

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
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        int count = function.getPointsCount();
        pw.print(count);
        for (int i = 0; i < count; i++) {
            pw.print(" " + function.getPointX(i) + " " + function.getPointY(i));
        }

        pw.flush();
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int count = (int) st.nval;
        double[] x = new double[count];
        double[] y = new double[count];
        for (int i = 0; i < count; i++) {
            st.nextToken();
            x[i] = st.nval;

            st.nextToken();
            y[i] = st.nval;
        }
        return new ArrayTabulatedFunction(x, y);
    }
}
