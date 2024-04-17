package com.jefftastic.pngtoascii;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Reads image data as pixels and compares
 * perceived grayscale brightness to a character map.
 */

public class ASCIIConverter {
    private static String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/|()1{}[]?-_+~<>i!lI;:,^`'. ";
    private static int asciiLen = ascii.length() - 1;

    public static String toASCII(Image image, int radius) {
        // Declare variables
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader pr = image.getPixelReader();
        StringBuilder result = new StringBuilder();

        // Iterate width-wise and populate array of bytes
        for (int y = 0; y < height / radius; y++) {
            for (int x = 0; x < width / radius; x++) {
                int value = RGBtoGS.getSample(pr, x, y, width, height, radius);
                result.append(gsToChar(value));
            }
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Converts 0-255 grayscale value into a proper
     * ASCII character.
     */
    private static char gsToChar(int value) {
        int pos = asciiLen * value / 255;
        return ascii.charAt(Math.abs(pos));
    }

    /**
     * Quick and dirty converter for RGB image data to a set of
     * grayscale bytes.
     */
    static class RGBtoGS {
        private static int getSample(PixelReader pr, int x, int y, int width, int height, int radius) {
            // Declare variables
            int realX = x * radius, realY = y * radius;
            int count = 0;
            long result = 0;

            // Convert into (likely large) grayscale value
            for (int radY = 0; radY < radius; radY++) {
                // Skip if we're out of image bounds (width wise)
                if (realY + radY > width)
                    continue;
                for (int radX = 0; radX < radius; radX++) {
                    // Skip if we're out of image bounds (height wise)
                    if (realX + radX > height)
                        continue;
                    // Convert current position into grayscale 0-255 value
                    Color argb = pr.getColor(realX + radX, realY + radY).grayscale();
                    result += argb.getOpacity() > 0.1 ? (long) ((argb.getRed() * 255 +
                                    argb.getBlue() * 255 +
                                    argb.getGreen() * 255) / 3) : 255;
                    count++;
                }
            }

            // Return average
            return (int) (result / Math.max(count, 1));
        }
    }
}
