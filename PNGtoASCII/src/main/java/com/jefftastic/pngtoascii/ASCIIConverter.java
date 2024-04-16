package com.jefftastic.pngtoascii;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

/**
 * Reads image data as pixels and compares
 * perceived grayscale brightness to a character map.
 */

public class ASCIIConverter {
    private static String ascii = "@&%QWNM0gB$#DR8mHXKAUbGOpV4d9h6PkqwSE2]ayjxY5Zoen[ult13If}C{iF|(7J)vTLs?z/*cr!+<>;=^,_:'-.` ";

    public static String toASCII(Image image, int radius) {
        // Declare variables
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader pr = image.getPixelReader();
        StringBuilder result = new StringBuilder();

        // Iterate width-wise and populate array of bytes
        for (int y = 0; y < height / radius; y++) {
            for (int x = 0; x < width / radius; x++) {
                byte value = RGBtoGS.getSample(pr, x, y, width, height, radius);
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
    private static char gsToChar(byte value) {
        return ascii.charAt((ascii.length() - 1) / Math.max(value, 1));
    }

    /**
     * Quick and dirty converter for RGB image data to a set of
     * grayscale bytes.
     */
    static class RGBtoGS {
        private static byte getSample(PixelReader pr, int x, int y, int width, int height, int radius) {
            // Declare variables
            int realX = x * radius, realY = y * radius;
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
                    int argb = Math.abs(pr.getArgb(realX + radX, realY + radY));
                    result += (long) ((((argb >> 16) & 0xff) * 0.21 +
                                    ((argb >> 8) & 0xff) * 0.72 +
                                    (argb & 0xff) * 0.07)) / 3;
                }
            }

            // Return average
            return (byte) (result / (radius * radius));
        }
    }
}
