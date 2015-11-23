package com.example.milan.scgenerator.generators;

import android.graphics.Bitmap;

import com.example.milan.scgenerator.services.GenerateService;

/**
 * Created by milan on 18.11.15..
 */
public class VideoGenerator extends ImageGenerator {
    /** Video width. */
    private int width;
    /** Video height. */
    private int height;

    @Override
    public void generate(final int number) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;

        for (int i = 0; i < number; i++) {
            super.save(Bitmap.createBitmap(width, height, config), "video", "mp4");
        }
    }


    // setters
    @Override
    public void setWidth(final int width) {
        this.width = width;
    }

    @Override
    public void setHeight(final int height) {
        this.height = height;
    }


}
