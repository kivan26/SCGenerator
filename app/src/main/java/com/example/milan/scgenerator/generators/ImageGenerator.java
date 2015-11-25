package com.example.milan.scgenerator.generators;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.milan.scgenerator.services.GenerateService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by milan on 18.11.15..
 */
public class ImageGenerator implements Generator {
    public final String root = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).toString();
    public Random random;
    private int stride; // must be >= WIDTH
    private int width;
    private int height;


    public ImageGenerator() {
        random = new Random();
    }


    @Override
    public void generate(int number) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        // stride must be >= width
        stride += (width + 1);

        // create how much images we need
        for (int i = 0; i < number; i++) {
            save(Bitmap.createBitmap(createColors(width, height), 0, stride, width, height, config)
                     , "image", "jpg", root);
        }
    }


    /**
     * Save image or video .
     * @param bitmap Bitmap
     * @param filename String
     * @param fileextension String
     * */
    public void save(Bitmap bitmap, String filename, String fileextension, String root) {
        File file = new File(root);
        String imageName = filename + "-" + random.nextInt(1000) + "." + fileextension;
        File image = new File(file, imageName);

        if (!image.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                bitmap.recycle();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Create colors for image.
     * @param width int
     * @param height int
     * */
    public int[] createColors(int width, int height) {
        int[] colors = new int[stride * height];
        try {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int r = x * 255 / (width - 1);
                    int g = y * 255 / (height
                            - 1);
                    int b = 255 - Math.min(r, g);
                    int a = Math.max(r, g);
                    colors[y * stride + x] = (a << 24) | (r << 16) | (g << 8) | b;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return colors;
    }



    // Setters
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
