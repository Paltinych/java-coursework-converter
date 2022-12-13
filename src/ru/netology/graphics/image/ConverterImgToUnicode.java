package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class ConverterImgToUnicode implements TextGraphicsConverter {

    private int width;
    private int height;
    private double maxRatio;
    TextColorSchema schema = new ColorScheme();

    public ConverterImgToUnicode() {
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        StringBuilder textImg = new StringBuilder();

        BufferedImage img = ImageIO.read(new URL(url));
        double ratio = ratio(img.getWidth(), img.getHeight());

        if (ratio > maxRatio) throw new BadImageSizeException(ratio, maxRatio);

        int newWidth = setWidth(img.getWidth(), img.getHeight(), ratio);
        int newHeight = setHeight(img.getWidth(), img.getHeight(), ratio);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];

                char symbol = schema.convert(color);
                textImg.append(symbol).append(symbol);
            }
            textImg.append("\n");
        }

        return textImg.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    // Расчет соотношения сторон
    public double ratio(int width, int height) {
        double ratio;
        if (width > height) {
            ratio = (double) width / height;
        } else {
            ratio = (double) height / width;
        }
        return ratio;
    }

    // Расчет новой ширины
    public int setWidth(int width, int height, double ratio) {
        int newWidth;
        if (width > this.width || height > this.height) {
            if (width > height) {
                newWidth = this.width;
            } else {
                newWidth = (int) (this.width / ratio);
            }
        } else {
            newWidth = width;
        }
        return newWidth;
    }

    // Расчет новой высоты
    public int setHeight(int width, int height, double ratio) {
        int newHeight;
        if (width > this.width || height > this.height) {
            if (width > height) {
                newHeight = (int) (this.height / ratio);
            } else {
                newHeight = this.height;
            }
        } else {
            newHeight = height;
        }
        return newHeight;
    }
}
