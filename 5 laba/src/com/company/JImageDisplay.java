package com.company;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

/**
 * Этот класс позволяет отображать фракталы
 * Данный класс является производным от javax.swing.JComponent.
 */
class JImageDisplay extends JComponent
{
    /**
     * экзмепляр класса Buffered Image.
     * управляет изображением, содержимое которого можно записать
     */
    private BufferedImage displayImage;

    /** Метод получает экранное изображение из другого класса. */
    public BufferedImage getImage() {
        return displayImage;
    }
    /**
     * конструктор JImageDisplay принимает целочисленные значения ширины и высоты,
     * инициализирует объект BufferedImage новым изображением с данными шириной и высотой,
     * принимая тип TYPE_INT_RGB
     */
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        /**
         * Вызов метода setPrefferedSize родительского класса с заданными шириной и высотой
         */
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);
    }
    /**
     * реализация суперкласса paintComponent
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(),
                displayImage.getHeight(), null);
    }
    /**
     * Делает все пиксели изображения черными
     */
    public void clearImage()
    {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }
    /**
     * делает пиксели определенного цвета
     */
    public void drawPixel(int x, int y, int rgbColor)
    {
        displayImage.setRGB(x, y, rgbColor);
    }
}