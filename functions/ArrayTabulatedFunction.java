package functions;

// класс, реализующий табулированную функцию с использованием массива для хранения точек
public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;  // массив для хранения точек функции
    private int count;               // текущее количество точек в массиве

    // вспомогательный метод для сравнения вещественных чисел с учетом погрешности
    private boolean equals(double a, double b) {
        return Math.abs(a - b) < 1e-10;
    }

    // проверка корректности индекса
    // выбрасывает исключение, если индекс выходит за границы массива точек
    private void checkIndex(int index) {
        if (index < 0 || index >= count) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " выходит за границы [0, " + (count - 1) + "]");
        }
    }

    // проверка упорядоченности при изменении точки
    private void checkOrder(int index, double newX) throws InappropriateFunctionPointException {
        if (index > 0 && newX <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Координата x должна быть больше предыдущей точки");
        }
        if (index < count - 1 && newX >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Координата x должна быть меньше следующей точки");
        }
    }

    // конструктор для создания функции с равномерной сеткой точек
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        // проверка корректности входных параметров
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        this.count = pointsCount;
        // создание массива с запасом для добавления новых точек
        this.points = new FunctionPoint[pointsCount + 5];
        // вычисление шага между соседними точками
        double step = (rightX - leftX) / (pointsCount - 1);
        // создание точек с координатами x и начальным значением y = 0
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    // конструктор для создания функции с равномерной сеткой и заданными значениями y
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        // проверка корректности входных параметров
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        this.count = values.length;
        // создание массива с запасом для добавления новых точек
        this.points = new FunctionPoint[count + 5];
        // вычисление шага между соседними точками
        double step = (rightX - leftX) / (count - 1);
        // создание точек с координатами x и значениями y из массива values
        for (int i = 0; i < count; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    // получаем левую границу области определения функции
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    // получаем правую границу области определения функции
        public double getRightDomainBorder() {
        return points[count - 1].getX();
    }

    // вычисляем значение функции в точке x с помощью линейной интерполяции
    // если x находится вне области определения, возвращаем Double.NaN
    public double getFunctionValue(double x) {
        // проверка, находится ли x в области определения функции
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // поиск интервала, в котором находится x
        for (int i = 0; i < count - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            // если x находится в текущем интервале [x1, x2]
            if (x >= x1 && x <= x2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                // линейная интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    // возвращаем количество точек в табулированной функции
    public int getPointsCount() {
        return count;
    }

    // получаем точку по указанному индексу
    // возвращаем копию точки для обеспечения инкапсуляции
    public FunctionPoint getPoint(int index) {
        // проверка корректности индекса
        checkIndex(index);
        // возвращам копию точки, чтобы избежать изменения внутреннего состояния
        return new FunctionPoint(points[index]);
    }

    // устанавливаем новую точку по указанному индексу
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        // проверка корректности индекса
        checkIndex(index);
        // проверка упорядоченности координаты x новой точки
        checkOrder(index, point.getX());
        // установка новой точки
        points[index] = new FunctionPoint(point);
    }

    // получаем координату x точки по указанному индексу
    public double getPointX(int index) {
        // проверка корректности индекса
        checkIndex(index);
        return points[index].getX();
    }

    // устанавливаем координату x точки по указанному индексу
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        // проверка корректности индекса
        checkIndex(index);
        // проверка упорядоченности нового значения x
        checkOrder(index, x);
        // установка нового значения x
        points[index].setX(x);
    }

    // получаем координату y точки по указанному индексу
    public double getPointY(int index) {
        // проверка корректности индекса
        checkIndex(index);
        return points[index].getY();
    }

    // устанавливаем координату y точки по указанному индексу
    public void setPointY(int index, double y) {
        // проверка корректности индекса
        checkIndex(index);
        points[index].setY(y);
    }

    // удаляем точку по указанному индексу
    // сдвигаем все последующие точки на одну позицию влево
    public void deletePoint(int index) {
        // проверка корректности индекса
        checkIndex(index);
        // проверка, что после удаления останется минимум 2 точки
        if (count < 3) {
            throw new IllegalStateException("Невозможно удалить точку: количество точек должно быть не менее 3");
        }

        // сдвиг элементов массива влево, начиная с позиции после удаляемой точки
        for (int i = index; i < count - 1; i++) {
            points[i] = points[i + 1];
        }
        // очистка последней позиции и уменьшение счетчика
        points[count - 1] = null;
        count--;
    }

    // добавляем новую точку в табулированную функцию
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        // поиск позиции для вставки (точки должны быть упорядочены по x)
        int insertIndex = 0;
        while (insertIndex < count && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }

        // проверка, что точка с таким x еще не существует
        if (insertIndex < count && equals(points[insertIndex].getX(), point.getX())) {
            throw new InappropriateFunctionPointException("Точка с координатой x = " + point.getX() + " уже существует");
        }

        // проверка необходимости увеличения размера массива
        if (count >= points.length) {
            // увеличение массива в 2 раза для эффективности
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            // копирование существующих точек в новый массив
            for (int i = 0; i < count; i++) {
                newPoints[i] = points[i];
            }
            points = newPoints;
        }

        // сдвиг элементов для освобождения места под новую точку
        for (int i = count; i > insertIndex; i--) {
            points[i] = points[i - 1];
        }
        // вставка новой точки и увеличение счетчика
        points[insertIndex] = new FunctionPoint(point);
        count++;
    }
}