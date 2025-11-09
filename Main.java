import functions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ TABULATEDFUNCTION ===\n");

        // тестирование ArrayTabulatedFunction
        System.out.println("=".repeat(50));
        System.out.println("ТЕСТИРОВАНИЕ ArrayTabulatedFunction");
        System.out.println("=".repeat(50));

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(0, 4, 5);
        testTabulatedFunction(arrayFunction, "ArrayTabulatedFunction");

        // тестирование LinkedListTabulatedFunction
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ТЕСТИРОВАНИЕ LinkedListTabulatedFunction");
        System.out.println("=".repeat(50));

        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(0, 4, 5);
        testTabulatedFunction(linkedListFunction, "LinkedListTabulatedFunction");

        // тестирование исключений
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ТЕСТИРОВАНИЕ ИСКЛЮЧЕНИЙ");
        System.out.println("=".repeat(50));

        testAllExceptions();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("ТЕСТИРОВАНИЕ ЗАВЕРШЕНО");
        System.out.println("=".repeat(50));
    }

    // универсальная функция для тестирования любой реализации TabulatedFunction
    private static void testTabulatedFunction(TabulatedFunction function, String functionType) {
        System.out.println("\n1. Создание функции y = x^2 для " + functionType);

        // устанавливаем значения y согласно квадратичной функции x^2
        for (int i = 0; i < function.getPointsCount(); i++) {
            double x = function.getPointX(i);
            function.setPointY(i, x * x);
        }

        // вывод начальных точек
        System.out.println("Начальные точки функции:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("(%.2f, %.2f)%n", function.getPointX(i), function.getPointY(i));
        }

        // 2. тестирование интерполяции до изменений
        System.out.println("\n2. Интерполяция на исходной функции");
        double[] testPoints = {0.5, 1.2, 2.5, 3.2, 6.8};

        for (double x : testPoints) {
            double y = function.getFunctionValue(x);
            System.out.printf("f(%.2f) = %.2f%n", x, y);
        }

        // 3. тестирование границ области определения
        System.out.println("\n3. Область определения");
        System.out.printf("Левая граница: %.2f%n", function.getLeftDomainBorder());
        System.out.printf("Правая граница: %.2f%n", function.getRightDomainBorder());

        // точки вне области определения
        System.out.println("Проверка точек вне области определения:");
        double[] outsidePoints = {-1.0, 10.0};
        for (double x : outsidePoints) {
            double y = function.getFunctionValue(x);
            System.out.printf("f(%.2f) = %s%n", x, Double.isNaN(y) ? "не определено" : String.format("%.2f", y));
        }

        // 4. добавление новых точек
        System.out.println("\n4. Добавление точек");
        try {
            // добавляем точку (1.5, 2.25) - это y = x² при x=1.5
            System.out.println("Добавляем точку (1.5, 2.25) - соответствует y = x²");
            function.addPoint(new FunctionPoint(1.5, 2.25));

            // добавляем точку (3.5, 12.25) - это y = x² при x=3.5
            System.out.println("Добавляем точку (3.5, 12.25) - соответствует y = x²");
            function.addPoint(new FunctionPoint(3.5, 12.25));

            System.out.println("После добавления двух точек:");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        // выводим обновленный список точек
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("(%.2f, %.2f)%n", function.getPointX(i), function.getPointY(i));
        }

        // 5. удаление точки
        System.out.println("\n5. Удаление точки");
        System.out.println("Удаляем точку с индексом 3");
        try {
            function.deletePoint(3);
            System.out.println("После удаления точки с индексом 3:");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении точки: " + e.getMessage());
        }

        // проверяем, что точка действительно удалена
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("(%.2f, %.2f)%n", function.getPointX(i), function.getPointY(i));
        }

        // 6. изменение точки
        System.out.println("\n6. Изменение точки");
        System.out.println("Изменяем точку с индексом 2 на (3.0, 4.0)");
        try {
            function.setPoint(2, new FunctionPoint(3.0, 4.0));
            System.out.println("После изменения точки с индексом 2:");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при изменении точки: " + e.getMessage());
        }

        // проверяем, что точка изменилась корректно
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("(%.2f, %.2f)%n", function.getPointX(i), function.getPointY(i));
        }

        // 7. финальная проверка интерполяции
        System.out.println("\n7. Финальная интерполяция");
        for (double x : testPoints) {
            double y = function.getFunctionValue(x);
            System.out.printf("f(%.2f) = %.2f%n", x, y);
        }

        // 8. итоговое состояние
        System.out.println("\n8. Итоговое состояние функции");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("(%.2f, %.2f)%n", function.getPointX(i), function.getPointY(i));
        }
    }

    private static void testAllExceptions() {
        // тест 1: некорректные параметры конструктора
        System.out.println("\nТест 1: Некорректные параметры конструктора");
        try {
            System.out.println("Пытаемся создать функцию с leftX=10, rightX=0 (нарушение порядка)");
            TabulatedFunction func = new ArrayTabulatedFunction(10, 0, 5);
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (IllegalArgumentException e) {
            System.out.println(":) IllegalArgumentException: " + e.getMessage());
        }

        try {
            System.out.println("Пытаемся создать функцию с pointsCount=1 (слишком мало точек)");
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 1);
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (IllegalArgumentException e) {
            System.out.println(":) IllegalArgumentException: " + e.getMessage());
        }

        // тест 2: выход за границы индексов
        System.out.println("\nТест 2: Выход за границы индексов");
        TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 3);

        try {
            System.out.println("Пытаемся получить точку с индексом -1");
            func.getPoint(-1);
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(":) FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }

        try {
            System.out.println("Пытаемся получить точку с индексом 10 (вне границ)");
            func.getPoint(10);
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println(":) FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }

        // тест 3: нарушение порядка точек
        System.out.println("\nТест 3: Нарушение порядка точек");
        try {
            System.out.println("Пытаемся установить точку (0.5, 2.0) на позицию 1 (нарушает порядок X)");
            func.setPoint(1, new FunctionPoint(0.5, 2.0));
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(":) InappropriateFunctionPointException: " + e.getMessage());
        }

        try {
            System.out.println("Пытаемся установить X=0.5 для точки с индексом 1 (нарушает порядок)");
            func.setPointX(1, 0.5);
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(":) InappropriateFunctionPointException: " + e.getMessage());
        }

        // тест 4: дублирование точек
        System.out.println("\nТест 4: Дублирование точек");
        try {
            System.out.println("Пытаемся добавить точку (5.0, 10.0) - X=5.0 уже существует");
            func.addPoint(new FunctionPoint(5.0, 10.0));
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (InappropriateFunctionPointException e) {
            System.out.println(":) InappropriateFunctionPointException: " + e.getMessage());
        }

        // тест 5: удаление при недостаточном количестве точек
        System.out.println("\nТест 5: Удаление при недостаточном количестве точек");
        try {
            System.out.println("Удаляем точку с индексом 0...");
            func.deletePoint(0);
            System.out.println("Удаляем точку с индексом 0 (теперь осталось 2 точки)...");
            func.deletePoint(0); // Теперь осталось 2 точки
            System.out.println("ОШИБКА: Исключение не было выброшено!");
        } catch (IllegalStateException e) {
            System.out.println(":) IllegalStateException: " + e.getMessage());
        }
    }
}