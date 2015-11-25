package com.example.milan.scgenerator.generators;

import android.content.ContentValues;
import android.content.Context;
import android.provider.CallLog;
import android.util.Log;

import java.util.Random;

/**
 * Created by milan on 25.11.15..
 */
public class CallGenerator implements Generator {
    private static final String TAG = "Call Generator";
    private Random random;
    private Context context;

    public CallGenerator(Context context){
        this.context = context;
        random = new Random();
    }

    @Override
    public void generate(int number) {
        ContentValues[] contentValues = new ContentValues[number];

        try {
            for (int i = 0; i < number; i++) {
                contentValues[i] = createCallLog();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, e.getMessage());
        }

        try {
            int insert = context.getContentResolver().bulkInsert(CallLog.Calls.CONTENT_URI, contentValues);
            Log.i(TAG, String.valueOf(insert));

        }catch (SecurityException e){
            Log.e(TAG, e.getMessage());
        }

    }

    /** Create call log. */
    private ContentValues createCallLog(){
        // incoming or outgoing type
        int callType = CallLog.Calls.INCOMING_TYPE;
        if(random.nextInt(2) == 0){
            callType = CallLog.Calls.OUTGOING_TYPE;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CallLog.Calls.NUMBER,random.nextInt(200000 - 100000) + 100000);
        contentValues.put(CallLog.Calls.DATE, System.currentTimeMillis());
        contentValues.put(CallLog.Calls.DURATION, random.nextInt(60 - 5) + 5);
        contentValues.put(CallLog.Calls.TYPE, callType);
        contentValues.put(CallLog.Calls.NEW, 1);
        contentValues.put(CallLog.Calls.CACHED_NAME, "");
        contentValues.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        contentValues.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");

        Log.i(TAG, "Inserting call log ");

        return contentValues;
    }



}
