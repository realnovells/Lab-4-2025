package functions;

import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TabulatedFunction func = new ArrayTabulatedFunction(2, 4, new double[]{4, 9, 16});

        try {
            new ArrayTabulatedFunction(3, 0, new double[]{0, 1, 4});
        } catch (IllegalArgumentException e) {
            System.out.println("Поймано IllegalArgumentException: " + e.getMessage());
        }

        try {
            new ArrayTabulatedFunction(0, 3, new double[]{0});
        } catch (IllegalArgumentException e) {
            System.out.println("Поймано IllegalArgumentException: " + e.getMessage());
        }

        try {
            func.addPoint(new FunctionPoint(4, 16));
            System.out.println("Точка добавлена: " + func.getPointCopy(func.getPointsCount() - 1).getX());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении новой точки: " + e.getMessage());
        }

        func.setPointY(1, 10);
        System.out.println("Y второй точки изменено на 10");
        System.out.println("Значение функции при x = 2.5: " + func.getFunctionValue(2.5));

        System.out.println("\nТекущее состояние функции:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPointCopy(i);
            System.out.println("(" + p.getX() + ", " + p.getY() + ")");
        }

        Sin sinFunc = new Sin();
        Cos cosFunc = new Cos();
        System.out.println("\nSin и Cos на [0, π] с шагом 0.1:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.1f: sin=%.4f, cos=%.4f%n",
                    x, sinFunc.getFunctionValue(x), cosFunc.getFunctionValue(x));
        }

        TabulatedFunction tabSin = TabulatedFunctions.tabulate(sinFunc, 0, Math.PI, 10);
        TabulatedFunction tabCos = TabulatedFunctions.tabulate(cosFunc, 0, Math.PI, 10);
        System.out.println("\nТабулированные значения Sin и Cos:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.1f: sin=%.4f, cos=%.4f%n",
                    x, tabSin.getFunctionValue(x), tabCos.getFunctionValue(x));
        }

        Function sumSquares = Functions.sum(
                Functions.power(tabSin, 2),
                Functions.power(tabCos, 2)
        );
        System.out.println("\nСумма квадратов табулированных функций:");
        for (double x = 0; x <= Math.PI; x += 0.1) {
            System.out.printf("x=%.1f: sumSquares=%.4f%n", x, sumSquares.getFunctionValue(x));
        }

        TabulatedFunction tabExp = TabulatedFunctions.tabulate(new Exp(), 0, 10, 11);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("exp_ser.ser"))) {
            oos.writeObject(tabExp);
        }

        TabulatedFunction readExp;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("exp_ser.ser"))) {
            readExp = (TabulatedFunction) ois.readObject();
        }

        System.out.println("\nСравнение исходной и считанной экспоненты:");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("x=%d: original=%.4f, read=%.4f%n",
                    i, tabExp.getFunctionValue(i), readExp.getFunctionValue(i));
        }

        TabulatedFunction tabLog = TabulatedFunctions.tabulate(new Log(Math.E), 0.0001, 10, 11);


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("log_ser.ser"))) {
            oos.writeObject(tabLog);
        }

        TabulatedFunction readLog;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log_ser.ser"))) {
            readLog = (TabulatedFunction) ois.readObject();
        }

        System.out.println("\nСравнение исходного и считанного логарифма:");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("x=%d: original=%.4f, read=%.4f%n",
                    i, tabLog.getFunctionValue(i), readLog.getFunctionValue(i));
        }
    }
}
