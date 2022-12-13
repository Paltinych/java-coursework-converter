package ru.netology.graphics.image;

public class ColorScheme implements TextColorSchema {
    private final char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};

    public ColorScheme() {
    }

    @Override
    public char convert(int color) {
        return symbols[(int) Math.floor(color / 256. * symbols.length)];
    }
}
