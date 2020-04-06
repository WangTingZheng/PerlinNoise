package com.wangtingzheng.perlinnoise.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author WangTingZheng
 * @date 2020/4/6 16:13
 * @features generate noise image with perlin noise
 */
public class GenerateImage {
    /**
     * get max value Perlin Noise matrix in order to set zoom value
     * @param value the matrix, int[]
     * @return max value
     */
    public static double getMax(double[] value)
    {
        double max = -1;
        for(int i =0; i<value.length ;i++)
        {
            if(value[i] > max){
                max = value[i];
            }
        }
        return max;
    }

    /**
     * get noise value and convert positive value
     * @param size the image width(height)
     * @param z the z value(convert from 3d)
     * @return a double matrix contain perlin noise
     */
    public static double[] formatPerlin(int size, double z)
    {
        double[] perlinMatrix = new double[size*size];
        PerlinNoise perlinNoise = new PerlinNoise();

        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                double perlin = perlinNoise.perlin(i, j, z);
                perlinMatrix[i*size+j] = perlin>=0?perlin:(-1)*perlin;
            }
        }
        return perlinMatrix;
    }

    /**
     * according to max value of perlin noise value, get zoom value to make matrix value in [0,255]
     * @param perlin a origin perlin noise matrix
     * @param size the image width(height)
     * @return a double zoom value, every origin value can * this value to be converted as a RGB value
     */
    public static double getZoom(double[] perlin, int size)
    {
        double max = getMax(perlin);
        return (255/max)-1;
    }

    /**
     * get rgb value from a perlin noise gray value
     * @param colorValue grag value, [0,255]
     * @return rge color value
     */
    public static int getRGB(int colorValue)
    {
        Color color = new Color(colorValue, colorValue, colorValue);
        return color.getRGB();
    }

    /**
     * get converted matrix can be used in image
     * @param size the image width(height)
     * @param z the z value(select a 2d interface)
     * @return a int matrix contain rgb value from perlin noise
     */
    public static int[] getRGBMatrix(int size, double z)
    {
        double[] perlin = formatPerlin(size, z);
        double zoom = getZoom(perlin, size);
        int[] matrix = new int[size*size];
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                int colorValue= (int)(perlin[i*size+j]*zoom);
                matrix[i*size+j] = getRGB(colorValue);
            }
        }
        return matrix;
    }

    /**
     * generate a noise image
     * @param size the image width(height)
     * @param path the image file save path
     * @param z the z value(select a 2d interface)
     */
    public static void generate(int size, String path, double z)
    {
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
        int[] gray = getRGBMatrix(size, z);
        bufferedImage.setRGB(0, 0, size, size, gray, 0, size);
        File file = new File(path);
        try {
            ImageIO.write(bufferedImage,"jpeg",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
