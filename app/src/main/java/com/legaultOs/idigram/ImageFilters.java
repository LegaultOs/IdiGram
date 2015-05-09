package com.legaultOs.idigram;

/**
 * Created by Legault on 20/04/2015.
 */

import android.graphics.Bitmap;
import android.graphics.Color;


public class ImageFilters {


    public Bitmap applyInvertEffect(Bitmap src) {
        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;

        int height = src.getHeight();
        int width = src.getWidth();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                pixelColor = src.getPixel(x, y);
                // guardamos el alpha sin modificar
                A = Color.alpha(pixelColor);
                // invertimos los colores
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // colocamos el pixel
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        return bmOut;
    }

    public Bitmap applyGreyscaleEffect(Bitmap src) {
        //valores constantes para la escala de gris
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int A, R, G, B;
        int pixel;


        int width = src.getWidth();
        int height = src.getHeight();


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);

                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // aplicamos la formula
                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // colocamos el pixel
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        return bmOut;
    }

    public Bitmap applyColorFilterEffect(Bitmap src, double red, double green, double blue) {

        int width = src.getWidth();
        int height = src.getHeight();
        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);
                // aplicamos el filtro en cada color
                A = Color.alpha(pixel);
                R = (int) (Color.red(pixel) * red);
                G = (int) (Color.green(pixel) * green);
                B = (int) (Color.blue(pixel) * blue);
                // colocamos el pixel
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        return bmOut;
    }

    public Bitmap applySepiaToningEffect(Bitmap src, double depth, double red, double green, double blue) {

        int width = src.getWidth();
        int height = src.getHeight();
        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // primero lo pasamos a escala de gris para aplicarle el color sepia
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;

        int A, R, G, B;
        int pixel;


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);

                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // aplicamos la formula del filtro gris
                B = G = R = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // aplicamos el color sepia
                R += (depth * red);
                if (R > 255) {
                    R = 255;
                }

                G += (depth * green);
                if (G > 255) {
                    G = 255;
                }

                B += (depth * blue);
                if (B > 255) {
                    B = 255;
                }

                // colocamos el pixel
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }

    public Bitmap applyContrastEffect(Bitmap src, double value) {

        int width = src.getWidth();
        int height = src.getHeight();
        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;

        double contrast = Math.pow((100 + value) / 100, 2);


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                //aplicamos la formula del contraste a cada pixel
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.green(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.blue(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // colocamos el pixel
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        return bmOut;
    }

    public Bitmap applyBrightnessEffect(Bitmap src, int value) {

        int width = src.getWidth();
        int height = src.getHeight();
        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // subimos o bajamos el nivel del color
                R += value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }

                G += value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }

                B += value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }

                // colocamos el pixel
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        return bmOut;
    }

    public Bitmap applyGaussianBlurEffect(Bitmap src, double valor) {
        double[][] GaussianBlurConfig = new double[][]{
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = valor;
        convMatrix.Offset = 0;

        return ConvolutionMatrix.computeConvolution(src, convMatrix);
    }

    public Bitmap applySharpenEffect(Bitmap src) {
        double[][] SharpConfig = new double[][]{
                {-1, -1, -1},
                {-1, 9, -1},
                {-1, -1, -1}
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(SharpConfig);
        convMatrix.Factor = 1;
        return ConvolutionMatrix.computeConvolution(src, convMatrix);
    }

    public Bitmap applyEdgeDetectionEffect(Bitmap src) {
        double[][] EdgeDetectionConfig = new double[][]{
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(EdgeDetectionConfig);
        convMatrix.Factor = 1;
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution(src, convMatrix);
    }

    public Bitmap applySmoothEffect(Bitmap src, double value) {
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(1);
        convMatrix.Matrix[1][1] = value;
        convMatrix.Factor = value + 8;
        convMatrix.Offset = 1;
        return ConvolutionMatrix.computeConvolution(src, convMatrix);
    }

    public Bitmap applyEmbossEffect(Bitmap src) {
        double[][] EmbossConfig = new double[][]{
                {-2, 0, 0},
                {0, 1, 0},
                {0, 0, 2}
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(EmbossConfig);
        convMatrix.Factor = 1;
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution(src, convMatrix);
    }

    public Bitmap applyEngraveEffect(Bitmap src) {
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(0);
        convMatrix.Matrix[0][0] = -2;
        convMatrix.Matrix[1][1] = 2;
        convMatrix.Factor = 1;
        convMatrix.Offset = 95;
        return ConvolutionMatrix.computeConvolution(src, convMatrix);
    }

    public Bitmap applySaturationFilter(Bitmap source, int level) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        // Creamos un BM como el original pero vacio
        Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
        float[] HSV = new float[3];


        float lv;
        if (level >= 10) lv = ((float) level) / 10;
        else {

            lv = ((float) 50 + level) / 100;
        }


        int pixel;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {

                pixel = source.getPixel(x, y);
                // convertimos el color a HSV (Hue,Saturation,Value)
                Color.colorToHSV(pixel, HSV);

                // aumentamos la saturacion
                HSV[1] = lv * HSV[1];


                HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));


                // colocamos el pixel
                bmOut.setPixel(x, y, Color.HSVToColor(HSV));

            }
        }


        return bmOut;
    }


}
