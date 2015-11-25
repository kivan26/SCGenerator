package com.example.milan.scgenerator.generators;

import android.content.ContentValues;
import android.content.Context;
import android.provider.Telephony;
import android.util.Log;

import com.example.milan.scgenerator.services.GenerateService;

import java.util.Random;

/**
 * Created by milan on 18.11.15..
 */
public class SmsGenerator implements Generator {
    private static final String TAG = "SMS Generator";
    public static final String ALPHABETS = "abcdefghijklmnoporstuvwxyz";
    public Random random;
    public Context context;
    public GenerateService service;

    public SmsGenerator(Context context, GenerateService service) {
        random = new Random();
        this.context = context;
        this.service = service;
    }


    @Override
    public void generate(int number) {
        ContentValues[] contentValues = new ContentValues[number];

        try {
            for (int i = 0; i < number; i++) {
                contentValues[i] = values(i);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, e.getMessage());
        }

        int insert = context.getContentResolver().bulkInsert(Telephony.Sms.Inbox.CONTENT_URI
                , contentValues);

        Log.v(TAG, String.valueOf(insert));
    }


    /**
     * Create SMS (ContentValues).
     * @param id
     * */
    public ContentValues values(int id) {
        ContentValues values = new ContentValues();
        values.put(Telephony.Sms.ADDRESS, String.valueOf(random.nextInt(20000 - 5000) + 5000));
        values.put(Telephony.Sms.BODY, message());
        values.put(Telephony.Sms.READ, true);
        //values.put(Telephony.Sms.THREAD_ID, 2);
        values.put(Telephony.Sms.TYPE, Telephony.Sms.MESSAGE_TYPE_INBOX);
        values.put(Telephony.Sms.SUBJECT, message());
        return values;
    }



    /**
     * Generete random message.
     * @return message
     * */
    public String message() {
        StringBuilder messageBuilder = new StringBuilder();

        int count = random.nextInt(60 - 10) + 10;
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                messageBuilder.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
            } else if (i == 5) {
                messageBuilder.append(" ");
            } else {
                messageBuilder.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
            }
        }
        return messageBuilder.toString();
    }



}
