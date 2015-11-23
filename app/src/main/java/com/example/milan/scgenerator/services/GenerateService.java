package com.example.milan.scgenerator.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.milan.scgenerator.GeneratorConstants;
import com.example.milan.scgenerator.generators.ContactGenerator;
import com.example.milan.scgenerator.generators.ImageGenerator;
import com.example.milan.scgenerator.generators.SmsGenerator;
import com.example.milan.scgenerator.generators.VideoGenerator;

import java.util.Arrays;

/**
 * Created by milan on 18.11.15..
 */
public class GenerateService extends Service {
    /** Binder. */
    private IBinder binder = new ServiceBinder();
    /** Image generator. */
    private ImageGenerator imageGenerator;
    /** Video generator. */
    private VideoGenerator videoGenerator;
    /** Contact generator. */
    private ContactGenerator contactGenerator;
    /** Sms generator. */
    private SmsGenerator smsGenerator;
    /** Default value of width and height. */
    private static final int DEFAULT_WIDTH_HEIGTH = 200;
    public int imageVideoWidth = DEFAULT_WIDTH_HEIGTH;
    public int imageVideoHeight = DEFAULT_WIDTH_HEIGTH;
    private static final int ARRAY_SIZE = 4;

    @Override
    public void onCreate() {
        imageGenerator = new ImageGenerator();
        videoGenerator = new VideoGenerator();
        contactGenerator = new ContactGenerator(this, this);
        smsGenerator = new SmsGenerator(this, this);

        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }


    public class ServiceBinder extends Binder {
        public GenerateService getService() {
            return GenerateService.this;
        }
    }



    /**
     * Method generate images.
     * @param num int */
    public void generateImages(final int num) {
        new GenerateTask(new boolean[]{false, false, true, false}, num).execute();
    }

    /**
     * Method generate Videoes.
     * @param num int */
    public void generateVideo(final int num) {
        new GenerateTask(new boolean[]{false, false, false, true}, num).execute();
    }

    /**
     * Method generate sms.
     * @param num int */
    public void generateSms(final int num) {
        new GenerateTask(new boolean[]{true, false, false, false}, num).execute();
    }

    /**
     * Method generate contacts.
     * @param num int */
    public void generateContacts(final int num) {
        new GenerateTask(new boolean[]{false, true, false, false}, num).execute();
    }


    /**
     * Generate images, videoes, sms and contacts together.
     * @param num int
     * @param swstatus boolean[] */
    public void generateAll(final int num, final boolean[] swstatus) {
        if(swstatus == null)
            return;

        new GenerateTask(swstatus, num).execute();
    }


    /**
     * Generate images, videos, sms and contacts on worker thread. */
    private class GenerateTask extends AsyncTask<Void, Void, Boolean> {
        private boolean[] status = new boolean[ARRAY_SIZE];
        private int num;

        public GenerateTask(boolean[] status, int num) {
            this.status = Arrays.copyOf(status, this.status.length);
            this.num = num;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(status[0]) {
                smsGenerator.generate(num);
            }
            if(status[1]) {
                contactGenerator.generate(num);
            }
            if(status[2]) {
                imageGenerator.setWidth(imageVideoWidth);
                imageGenerator.setHeight(imageVideoHeight);
                imageGenerator.generate(num);
            }
            if(status[3]) {
                videoGenerator.setWidth(imageVideoWidth);
                videoGenerator.setHeight(imageVideoHeight);
                videoGenerator.generate(num);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean status) {
            finish();
        }

    }



    /**
     * Send state failed to PageImageView fragment via broadcast. */
    public void failed() {
        sendBroadcastToActivity(GeneratorConstants.BROADCAST_ACTION_FAILED);
    }
    /**
     * Send state finish to PageImageView fragment via broadcast. */
    public void finish() {
        sendBroadcastToActivity(GeneratorConstants.BROADCAST_ACTION_FINISHED);
    }


    /**
     * Sms reciver. */
    public static class DeliverReciver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {

        }
    }
    /**
     * MMS recive. */
    public static class MMDeliverReciver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {

        }
    }


    /**
     * Send broadcast extra to the home fragment.
     * @param state String */
    private void sendBroadcastToActivity(final String state) {
        Intent intent = new Intent(GeneratorConstants.BROADCAST_EXTRA_ORDER);
        intent.putExtra(GeneratorConstants.BROADCAST_EXTRA_GET_ORDER, state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



}
