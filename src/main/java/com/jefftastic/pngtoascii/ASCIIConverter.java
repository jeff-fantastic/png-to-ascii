package com.jefftastic.pngtoascii;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * Reads image data as pixels and compares
 * perceived grayscale brightness to a character map.
 */

public class ASCIIConverter {
    private static String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/|()1{}[]?-_+~<>i!lI;:,^`'. ";
    private static int asciiLen = ascii.length() - 1;

    /**
     * Converts image pixel data into ASCII.
     * @param image Image data
     * @param radius Square radius of pixels to sample
     * @param lineControl Lines to skip per successful line generation
     * @return String of the generated ASCII art
     */
    public static String toASCII(Image image, int radius, byte lineControl) {
        // Declare variables
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        int lineCount = 0;
        PixelReader pr = image.getPixelReader();
        StringBuilder result = new StringBuilder();

        // Iterate width-wise and populate array of bytes
        for (int y = 0; y < height / radius; y++) {
            // Skip line if count goes against control
            if (lineCount % (lineControl+1) != 0) {
                lineCount++;
                continue;
            }

            // Get sample and append character based on sample
            for (int x = 0; x < width / radius; x++) {
                short value = RGBtoGS.getSample(pr, x, y, width, height, radius);
                result.append(gsToChar(value));
            }

            // New line
            result.append("\n");
            lineCount++;
        }

        return result.toString();
    }

    /**
     * Converts 0-255 grayscale value into a proper
     * ASCII character.
     */
    private static char gsToChar(short value) {
        short pos = (short) (asciiLen * value / 255);
        return ascii.charAt(Math.abs(pos));
    }

    /**
     * Quick and dirty converter for RGB image data to a set of
     * grayscale bytes.
     */
    static class RGBtoGS {
        /**
         * Determines the brightness average of a square radius of pixels in an image
         * @param pr Pixel reader
         * @param x Starting X coordinate on the image
         * @param y Starting Y coordinate on the image
         * @param width Width of the image
         * @param height Height of the image
         * @param radius Radius of pixels to scan
         * @return The brightness average
         */
        private static short getSample(PixelReader pr, int x, int y, int width, int height, int radius) {
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
            return (short) (result / Math.max(count, 1));
        }

        /**
         * Converts an image into grayscale.
         * @param image Image data
         * @return Grayscale converted image
         */
        public static Image imageToGrayscale(Image image) {
            // Declare variables
            WritableImage writableImage = new WritableImage(
                    (int) image.getWidth(),
                    (int) image.getHeight()
            );
            PixelReader pr = image.getPixelReader();
            PixelWriter pw = writableImage.getPixelWriter();

            // Iterate and convert pixels
            for (int x = 0; x < writableImage.getWidth(); x++)
                for (int y = 0; y < writableImage.getHeight(); y++)
                    pw.setColor(x, y, pr.getColor(x, y).grayscale());
            return writableImage;
        }
    }

    /**
     * Performs generic filter operations on
     * image data, sorta similar to RGBtoGS.
     * @see RGBtoGS
     */
    static class ImageOp {

        /**
         * Modifies brightness and contrast values on
         * an image based on provided values.
         * @param image Input image
         * @param brightCoeff Brightness range from [-1.0 -> 1.0]
         * @param contrastCoeff Contrast range from [0 -> 255]
         * @return Modified image
         */
        public static Image modifyImage(Image image, float brightCoeff, short contrastCoeff) {
            // Declare variables
            RescaleOp rescaleOp = new RescaleOp(brightCoeff, contrastCoeff, null);

            // Apply filter and return
            BufferedImage bufImage = rescaleOp.filter(SwingFXUtils.fromFXImage(image, null), null);
            return SwingFXUtils.toFXImage(bufImage, null);
        }
    }
}
