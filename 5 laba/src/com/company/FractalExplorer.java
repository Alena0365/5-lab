package com.company;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.awt.image.*;

/**
 * Данный класс позволяет исследовать различные области фрактала
 * создание и отображение графического интерфейса Swing и обработка событий,
 * вызванных различными взаимодействиями с пользователем.
 */
public class FractalExplorer {
    /** Целое число «размер экрана», которое является шириной
     *и высотой отображения в пикселях
     */
    private int displaySize;
    /**
     * Ссылка JImageDisplay, для обновления отображения в разных
     * методах в процессе вычисления фрактала.
     */
    private JImageDisplay display;
    /**
     * Объект FractalGenerator. Будет использоваться ссылка на базовый класс
     * для отображения других видов фракталов в будущем.
     */
    private FractalGenerator fractal;
    /**
     * Объект Rectangle2D.Double, указывающий диапазона комплексной
     * плоскости, которая выводится на экран.
     */
    private Rectangle2D.Double range;
    /**
     * конструктор, который принимает значение размера отображения
     * в качестве аргумента, затем сохраняет это значение в соответствующем
     * поле, а также инициализирует объекты диапазона и фрактального генератора.
     */
    public FractalExplorer(int size) {
        displaySize = size;

        /** Инициализация фрактал-генератор и объекты диапазона. **/
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }
    /**
     * Метод, который инициализирует графический интерфейс Swing: JFrame,
     * содержащий объект JimageDisplay, кнопку для сброса отображения, выбора
     * фрактала и кнопку сохранения.
     */
    public void createAndShowGUI() {
        /**
         * Добавляем объект отображения изображения в позицию BorderLayout.CENTER
         */
        display.setLayout(new BorderLayout());
        JFrame myframe = new JFrame("Fractal Explorer");

        myframe.add(display, BorderLayout.CENTER);

        /** Создание кнопки сброса. */
        JButton resetButton = new JButton("Reset");

        /** Экземпляр ButtonHandler для кнопки reset. */
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        /** Экземпляр для MouseHandler */
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        /** Операция закрытия окна по умолчанию */
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /** поле со списком. */
        JComboBox Cb = new JComboBox();

        /** Добавление каждого объекта фрактального типа в поле со списком. */
        FractalGenerator mandelbrotFr = new Mandelbrot();
        Cb.addItem(mandelbrotFr);
        FractalGenerator tricornFr = new Tricorn();
        Cb.addItem(tricornFr);
        FractalGenerator burningShipFr = new BurningShip();
        Cb.addItem(burningShipFr);

        /** Экземпляр ButtonHandler в поле со списком. */
        ButtonHandler FrCh = new ButtonHandler();
        Cb.addActionListener(FrCh);

        /**
         * Создает новые объекты JPanel, JLabel, JComboBox, и добавляет
         * панель в кадр в положении NORTH.
         */
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel("Fractal:");
        newPanel.add(newLabel);
        newPanel.add(Cb);
        myframe.add(newPanel, BorderLayout.NORTH);

        /**
         * Создание кнопки сохранения, добавление кнопки в JPanel в положение
         * BorderLayout.SOUTH вместе с кнопкой reset.
         */
        JButton saveButton = new JButton("Save");
        JPanel BtmPanel = new JPanel();
        BtmPanel.add(saveButton);
        BtmPanel.add(resetButton);
        myframe.add(BtmPanel, BorderLayout.SOUTH);

        /** Экземпляр ButtonHandler для кнопки сохранения. */
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        /**
         *Данные операции правильно разместят содержимое окна, сделают его
         * видимым и запретят изменение размеров окна
         */
        myframe.pack();
        myframe.setVisible(true);
        myframe.setResizable(false);

    }

    /**
     *Метод должен циклически проходить через каждый пиксель в отображении
     * (т.е. значения x и y будут меняться от 0 до размера отображения),
     *
     */
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                /**
                 * Определяет координаты с плавающей точкой для определенного
                 * набора координат пикселей
                 */
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, y);
                /**
                * Вычисляем количество итераций для соответствующих
                * координат в области отображения фрактала.
                */
                int iteration = fractal.numIterations(xCoord, yCoord);

                /** Если число итераций равно -1, устанавливает пиксель в черный цвет. */
                if (iteration == -1) {
                    display.drawPixel(x, y, 0);
                } else {
                    /**
                     * Иначе выбирает значение цвета, основанное на количестве итераций.
                     */
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    /** Обновляем отображение в соответствии с цветом каждого пикселя */
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        /**
         * После того, как закончили отрисовывать все пиксели,
         * необходимо обновить JimageDisplay в соответствии с текущим
         * изображением.
         */
        display.repaint();
    }

    /**
     * класс для обработки событий java.awt.event.ActionListener от кнопки сброса.
     */
    private class ButtonHandler implements ActionListener {
        /**
         * Обработчик сбрасывает диапазон к начальному, определенному
         * генератором, а затем перерисовывает фрактал.
         */
        public void actionPerformed(ActionEvent e) {
            /** Получаем источник движения. */
            String command = e.getActionCommand();

            /**
             * Если источником является поле со списком, то выводится,
             * выбранный пользователем фрактал.
             */
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

            }
            /**
             * Если источником является кнопка reset, то выводится
             * первоначальное положение фрактала.
             */
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            /**
             * Если источником является кнопка save, то сохраняется
             * текущий фрактал.
             */
            else if (command.equals("Save")) {

                /** Позволяет пользователю выбирать файл для сохранения. */
                JFileChooser myFileChooser = new JFileChooser();

                /** Сохраняет только PNG изображения. */
                FileFilter extensionFilter =
                        new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                /** Проводник не позволяет выбирать не PNG файлы. */
                myFileChooser.setAcceptAllFileFilterUsed(false);

                /**
                 * Появляется окно "Сохранить файл", которое позволяет
                 * пользователю выбрать каталог и файл для сохранения.
                 */
                int userSelection = myFileChooser.showSaveDialog(display);

                /**
                 * Если результатом операции выбора файла является
                 * APPROVE_OPTION, продолжает операцию сохранения файла.
                 */
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    /** Получает файл и его имя. */
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    /** попытка сохранить фрактал на диск. */
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    /**
                     * Выдаёт сообщение с ошибкой
                     * если найдено исключение.
                     * */ catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                /**
                 * Если результатом операции выбора файла не является
                 * APPROVE_OPTION, возвращает значение.
                 */
                else return;
            }
        }
    }

    /**
     * класс для обработки событий java.awt.event.MouseListener с дисплея
     */
    private class MouseHandler extends MouseAdapter {
        /**
         * При получении события о щелчке мышью, класс отображает
         * пиксельные кооринаты щелчка в область фрактала, а затем вызывает
         * метод генератора recenterAndZoomRange() с координатами,
         * по которым щелкнули, и масштабом 0.5.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            /** Получает x координаты области экрана при клике мыши. */
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            /** Получает y координаты области экрана при клике мыши. */
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            /**
             * Вызов метода recenterAndZoomRange()
             */
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            /**
             * перерисовка фрактала
             */
            drawFractal();
        }
    }

    /**
     * Статический метод main(). Инициализация нового экземпляра класса
     * FractalExplorer с размером изображения 600(800 вылезает за рамки экрана). Вызов метода
     * createAndShowGUI () класса FractalExplorer. Вызов метода
     * drawFractal() класса FractalExplorer для отображения начального
     * представления.
     */
    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
