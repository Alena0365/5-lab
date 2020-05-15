package com.company;

import java.awt.geom.Rectangle2D;
/**
 * Этот класс является подклассом класса FractalGenerator
 */
public class Tricorn extends FractalGenerator
{
    /** Константа для числа максимальных итераций. */
    public static final int MAX_ITERATIONS = 2000;
    /**
     * Данный метод позволяет генератору фракталов определить наиболее "интересную" область
     * комплексной плоскости для конкретного фрактала. В качестве аргумента передается
     * прямоугольный объект, метод изменяет поля прямоугольника для отображения правильного
     * начального диапазона для фрактала. Устанавливает x=-2, y=-2, а их ширину и высоту равными 4.
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }
    /**
     * данный метод реализует итеративную функцию для фрактала Трикон.
     * Необходимо реализовать эту функцию, используя отдельные переменные для действительной и мнимой частей.
     *
     */
    public int numIterations(double x, double y)
    {
        /** устанавливаем нулевую итерацию */
        int iteration = 0;
        /** инициализируем действительную и мнимую части */
        double zreal = 0;
        double zimaginary = 0;
        /**
         * Вычисляем Zn = Zn-1^2 + c , где значения - комплексные числа
         * zreal and zimaginary, Z0=0, и c это конкретная точка фрактала
         * который отображается.  Повторяется пока модуль Z^2 > 4
         * или достигнуто максимальное количество итераций
         */
        while (iteration < MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = -2 * zreal * zimaginary + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }
        /**
         *Если превышено число итераций, возвращаем -1
         */
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }

    /** Возвращаем название фрактала. */
    public String toString() {
        return "Tricorn";
    }

}
