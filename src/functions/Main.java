package functions;

import functions.basic.*;
import functions.meta.*;
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
            System.out.println("Точка добавлена: " + func.getPoint(func.getPointsCount() - 1).getX());
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении новой точки: " + e.getMessage());
        }

        func.setPointY(1, 10);
        System.out.println("Y второй точки изменено на 10");
        System.out.println("Значение функции при x = 2.5: " + func.getFunctionValue(2.5));

        System.out.println("\nТекущее состояние функции:");
        for (int i = 0; i < func.getPointsCount(); i++) {
            FunctionPoint p = func.getPoint(i);
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
        TabulatedFunctions.writeTabulatedFunction(tabExp, new FileWriter("exp.txt"));
        TabulatedFunction readExp = TabulatedFunctions.readTabulatedFunction(new FileReader("exp.txt"));

        System.out.println("\nСравнение исходной и считанной экспоненты:");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("x=%d: original=%.4f, read=%.4f%n",
                    i, tabExp.getFunctionValue(i), readExp.getFunctionValue(i));
        }

        TabulatedFunction tabLog = TabulatedFunctions.tabulate(new Log(Math.E), 1, 10, 11);
        TabulatedFunctions.outputTabulatedFunction(tabLog, new FileOutputStream("log.bin"));
        TabulatedFunction readLog = TabulatedFunctions.inputTabulatedFunction(new FileInputStream("log.bin"));

        System.out.println("\nСравнение исходного и считанного логарифма (бинарный файл):");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("x=%d: original=%.4f, read=%.4f%n",
                    i, tabLog.getFunctionValue(i), readLog.getFunctionValue(i));
        }

        Exp expFunc = new Exp();
        Log lnFunc = new Log(Math.E);
        Function lnOfExpFunc = new Composition(lnFunc, expFunc);

        TabulatedFunction lnOfExp = TabulatedFunctions.tabulate(lnOfExpFunc, 0, 10, 11);

        System.out.println("\nСравнение композиции ln(exp(x)):");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("x=%d: ln(exp(x))=%.4f%n", i, lnOfExp.getFunctionValue(i));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("lnOfExp.ser"))) {
            oos.writeObject(lnOfExp);
        }

        TabulatedFunction readLnOfExp;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("lnOfExp.ser"))) {
            readLnOfExp = (TabulatedFunction) ois.readObject();
        }

        System.out.println("\nСравнение исходного и считанного ln(exp(x)) после сериализации:");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("x=%d: original=%.4f, read=%.4f%n",
                    i, lnOfExp.getFunctionValue(i), readLnOfExp.getFunctionValue(i));
        }
    }
}
